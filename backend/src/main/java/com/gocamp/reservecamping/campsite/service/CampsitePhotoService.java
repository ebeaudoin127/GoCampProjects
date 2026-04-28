// ============================================================
// Fichier : CampsitePhotoService.java
// Chemin : backend/src/main/java/com/gocamp/reservecamping/campsite/service
// Dernière modification : 2026-04-18
//
// Résumé :
// - Service métier pour les photos de site
// - Upload, compression, miniature, suppression, photo principale
// - Limite : 3 photos maximum par site
// ============================================================

package com.gocamp.reservecamping.campsite.service;

import com.gocamp.reservecamping.campsite.dto.CampsitePhotoResponse;
import com.gocamp.reservecamping.campsite.model.Campsite;
import com.gocamp.reservecamping.campsite.model.CampsitePhoto;
import com.gocamp.reservecamping.campsite.repository.CampsitePhotoRepository;
import com.gocamp.reservecamping.campsite.repository.CampsiteRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.*;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CampsitePhotoService {

    private static final long MAX_FILE_SIZE_BYTES = 5L * 1024L * 1024L;
    private static final int MAX_PHOTOS_PER_SITE = 3;
    private static final int MAX_MAIN_WIDTH = 1600;
    private static final int MAX_THUMB_WIDTH = 450;

    private final CampsiteRepository campsiteRepository;
    private final CampsitePhotoRepository campsitePhotoRepository;

    public CampsitePhotoService(
            CampsiteRepository campsiteRepository,
            CampsitePhotoRepository campsitePhotoRepository
    ) {
        this.campsiteRepository = campsiteRepository;
        this.campsitePhotoRepository = campsitePhotoRepository;
    }

    public List<CampsitePhotoResponse> getByCampsite(Long campsiteId) {
        campsiteRepository.findById(campsiteId)
                .orElseThrow(() -> new RuntimeException("Site introuvable."));

        return campsitePhotoRepository.findByCampsiteIdOrderByDisplayOrderAscIdAsc(campsiteId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CampsitePhotoResponse uploadPhoto(
            Long campsiteId,
            MultipartFile file,
            String captionFr,
            String captionEn,
            Boolean isPrimary
    ) {
        Campsite campsite = campsiteRepository.findById(campsiteId)
                .orElseThrow(() -> new RuntimeException("Site introuvable."));

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Aucun fichier reçu.");
        }

        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new RuntimeException("Le fichier dépasse la limite de 5 Mo.");
        }

        long existingCount = campsitePhotoRepository.countByCampsiteId(campsiteId);
        if (existingCount >= MAX_PHOTOS_PER_SITE) {
            throw new RuntimeException("Maximum de 3 photos par site.");
        }

        String contentType = file.getContentType();
        if (contentType == null ||
                !(contentType.equalsIgnoreCase("image/jpeg")
                        || contentType.equalsIgnoreCase("image/jpg")
                        || contentType.equalsIgnoreCase("image/png"))) {
            throw new RuntimeException("Format accepté : JPG, JPEG ou PNG uniquement.");
        }

        try {
            BufferedImage originalImage = ImageIO.read(file.getInputStream());
            if (originalImage == null) {
                throw new RuntimeException("Impossible de lire l'image.");
            }

            BufferedImage optimizedMain = resizeImage(originalImage, MAX_MAIN_WIDTH);
            BufferedImage optimizedThumb = resizeImage(originalImage, MAX_THUMB_WIDTH);

            Path originalDir = Paths.get("uploads/campsites/" + campsiteId + "/original");
            Path thumbDir = Paths.get("uploads/campsites/" + campsiteId + "/thumb");

            Files.createDirectories(originalDir);
            Files.createDirectories(thumbDir);

            String baseName = UUID.randomUUID().toString();

            String mainFileName = baseName + ".jpg";
            String thumbFileName = baseName + "_thumb.jpg";

            Path mainPath = originalDir.resolve(mainFileName);
            Path thumbPath = thumbDir.resolve(thumbFileName);

            writeJpeg(optimizedMain, mainPath, 0.82f);
            writeJpeg(optimizedThumb, thumbPath, 0.76f);

            CampsitePhoto photo = new CampsitePhoto();
            photo.setCampsite(campsite);
            photo.setFilePath("/uploads/campsites/" + campsiteId + "/original/" + mainFileName);
            photo.setThumbnailPath("/uploads/campsites/" + campsiteId + "/thumb/" + thumbFileName);
            photo.setCaptionFr(captionFr);
            photo.setCaptionEn(captionEn);
            photo.setDisplayOrder((int) existingCount + 1);

            boolean makePrimary = Boolean.TRUE.equals(isPrimary)
                    || campsitePhotoRepository.findByCampsiteIdAndIsPrimaryTrue(campsiteId).isEmpty();

            if (makePrimary) {
                clearPrimaryForCampsite(campsiteId);
                photo.setPrimary(true);
            } else {
                photo.setPrimary(false);
            }

            campsitePhotoRepository.save(photo);

            return toResponse(photo);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du traitement de l'image.", e);
        }
    }

    public void deletePhoto(Long photoId) {
        CampsitePhoto photo = campsitePhotoRepository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("Photo introuvable."));

        Long campsiteId = photo.getCampsite().getId();
        boolean wasPrimary = photo.isPrimary();

        deletePhysicalFile(photo.getFilePath());
        deletePhysicalFile(photo.getThumbnailPath());

        campsitePhotoRepository.delete(photo);

        if (wasPrimary) {
            List<CampsitePhoto> remaining = campsitePhotoRepository.findByCampsiteIdOrderByDisplayOrderAscIdAsc(campsiteId);
            if (!remaining.isEmpty()) {
                CampsitePhoto next = remaining.get(0);
                next.setPrimary(true);
                campsitePhotoRepository.save(next);
            }
        }
    }

    public void setPrimary(Long photoId) {
        CampsitePhoto photo = campsitePhotoRepository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("Photo introuvable."));

        Long campsiteId = photo.getCampsite().getId();

        clearPrimaryForCampsite(campsiteId);
        photo.setPrimary(true);

        campsitePhotoRepository.save(photo);
    }

    private void clearPrimaryForCampsite(Long campsiteId) {
        List<CampsitePhoto> photos = campsitePhotoRepository.findByCampsiteIdOrderByDisplayOrderAscIdAsc(campsiteId);
        for (CampsitePhoto p : photos) {
            if (p.isPrimary()) {
                p.setPrimary(false);
                campsitePhotoRepository.save(p);
            }
        }
    }

    private BufferedImage resizeImage(BufferedImage source, int maxWidth) {
        int originalWidth = source.getWidth();
        int originalHeight = source.getHeight();

        if (originalWidth <= maxWidth) {
            return convertToRgb(source);
        }

        int newWidth = maxWidth;
        int newHeight = (int) Math.round((double) originalHeight * newWidth / originalWidth);

        BufferedImage resized = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resized.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.drawImage(source, 0, 0, newWidth, newHeight, null);
        g2d.dispose();

        return resized;
    }

    private BufferedImage convertToRgb(BufferedImage source) {
        if (source.getType() == BufferedImage.TYPE_INT_RGB) {
            return source;
        }

        BufferedImage rgbImage = new BufferedImage(
                source.getWidth(),
                source.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );

        Graphics2D g2d = rgbImage.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, rgbImage.getWidth(), rgbImage.getHeight());
        g2d.drawImage(source, 0, 0, null);
        g2d.dispose();

        return rgbImage;
    }

    private void writeJpeg(BufferedImage image, Path path, float quality) throws IOException {
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
        if (!writers.hasNext()) {
            throw new IOException("Aucun writer JPEG disponible.");
        }

        ImageWriter writer = writers.next();

        try (ImageOutputStream ios = ImageIO.createImageOutputStream(path.toFile())) {
            writer.setOutput(ios);

            ImageWriteParam param = writer.getDefaultWriteParam();
            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(quality);
            }

            writer.write(null, new IIOImage(image, null, null), param);
        } finally {
            writer.dispose();
        }
    }

    private void deletePhysicalFile(String webPath) {
        if (webPath == null || webPath.isBlank()) return;

        String normalized = webPath.startsWith("/") ? webPath.substring(1) : webPath;
        Path filePath = Paths.get(normalized);

        try {
            Files.deleteIfExists(filePath);
        } catch (IOException ignored) {
        }
    }

    private CampsitePhotoResponse toResponse(CampsitePhoto photo) {
        return new CampsitePhotoResponse(
                photo.getId(),
                photo.getCampsite().getId(),
                photo.getFilePath(),
                photo.getThumbnailPath(),
                photo.isPrimary(),
                photo.getDisplayOrder(),
                photo.getCaptionFr(),
                photo.getCaptionEn(),
                photo.isActive()
        );
    }
}