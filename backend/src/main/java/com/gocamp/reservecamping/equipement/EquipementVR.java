// ============================================================
// Fichier : EquipementVR.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/equipement
// Dernière modification : 2026-05-09
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Entité JPA des équipements VR
// - Ajout des options de recherche par défaut
// - Services souhaités : eau, électricité, égout
// - Ampérages souhaités : 15/20, 30, 50 amp
//
// Historique des modifications :
// 2026-05-09
// - Ajout defaultRequiresWater
// - Ajout defaultRequiresElectricity
// - Ajout defaultRequiresSewer
// - Ajout defaultRequires15_20Amp
// - Ajout defaultRequires30Amp
// - Ajout defaultRequires50Amp
// ============================================================

package com.gocamp.reservecamping.equipement;

import com.gocamp.reservecamping.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "equipement_vr")
public class EquipementVR {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 60)
    private String marque;

    @Column(length = 60)
    private String modele;

    private Integer longueur;

    @Column(length = 60)
    private String noSerie;

    @Column(length = 30)
    private String noPlaque;

    private Boolean hasExtension = false;

    private Integer extensionConducteur;

    private Integer extensionPassager;

    private Boolean actif = true;

    @Column(name = "default_requires_water", nullable = false)
    private Boolean defaultRequiresWater = false;

    @Column(name = "default_requires_electricity", nullable = false)
    private Boolean defaultRequiresElectricity = false;

    @Column(name = "default_requires_sewer", nullable = false)
    private Boolean defaultRequiresSewer = false;

    @Column(name = "default_requires_15_20_amp", nullable = false)
    private Boolean defaultRequires15_20Amp = false;

    @Column(name = "default_requires_30_amp", nullable = false)
    private Boolean defaultRequires30Amp = false;

    @Column(name = "default_requires_50_amp", nullable = false)
    private Boolean defaultRequires50Amp = false;

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getMarque() {
        return marque;
    }

    public String getModele() {
        return modele;
    }

    public Integer getLongueur() {
        return longueur;
    }

    public String getNoSerie() {
        return noSerie;
    }

    public String getNoPlaque() {
        return noPlaque;
    }

    public Boolean getHasExtension() {
        return hasExtension;
    }

    public Integer getExtensionConducteur() {
        return extensionConducteur;
    }

    public Integer getExtensionPassager() {
        return extensionPassager;
    }

    public Boolean getActif() {
        return actif;
    }

    public Boolean getDefaultRequiresWater() {
        return defaultRequiresWater;
    }

    public Boolean getDefaultRequiresElectricity() {
        return defaultRequiresElectricity;
    }

    public Boolean getDefaultRequiresSewer() {
        return defaultRequiresSewer;
    }

    public Boolean getDefaultRequires15_20Amp() {
        return defaultRequires15_20Amp;
    }

    public Boolean getDefaultRequires30Amp() {
        return defaultRequires30Amp;
    }

    public Boolean getDefaultRequires50Amp() {
        return defaultRequires50Amp;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public void setLongueur(Integer longueur) {
        this.longueur = longueur;
    }

    public void setNoSerie(String noSerie) {
        this.noSerie = noSerie;
    }

    public void setNoPlaque(String noPlaque) {
        this.noPlaque = noPlaque;
    }

    public void setHasExtension(Boolean hasExtension) {
        this.hasExtension = hasExtension;
    }

    public void setExtensionConducteur(Integer extensionConducteur) {
        this.extensionConducteur = extensionConducteur;
    }

    public void setExtensionPassager(Integer extensionPassager) {
        this.extensionPassager = extensionPassager;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public void setDefaultRequiresWater(Boolean defaultRequiresWater) {
        this.defaultRequiresWater = defaultRequiresWater;
    }

    public void setDefaultRequiresElectricity(Boolean defaultRequiresElectricity) {
        this.defaultRequiresElectricity = defaultRequiresElectricity;
    }

    public void setDefaultRequiresSewer(Boolean defaultRequiresSewer) {
        this.defaultRequiresSewer = defaultRequiresSewer;
    }

    public void setDefaultRequires15_20Amp(Boolean defaultRequires15_20Amp) {
        this.defaultRequires15_20Amp = defaultRequires15_20Amp;
    }

    public void setDefaultRequires30Amp(Boolean defaultRequires30Amp) {
        this.defaultRequires30Amp = defaultRequires30Amp;
    }

    public void setDefaultRequires50Amp(Boolean defaultRequires50Amp) {
        this.defaultRequires50Amp = defaultRequires50Amp;
    }
}
