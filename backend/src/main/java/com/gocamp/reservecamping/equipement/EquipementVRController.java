package com.gocamp.reservecamping.equipement;

import com.gocamp.reservecamping.equipement.dto.EquipementVRDTO;
import com.gocamp.reservecamping.equipement.dto.EquipementVRRequest;
import com.gocamp.reservecamping.security.UserDetailsImpl;
import com.gocamp.reservecamping.user.User;
import com.gocamp.reservecamping.user.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/me/equipements")
@CrossOrigin(origins = "*")
public class EquipementVRController {

    private final EquipementVRService equipementVRService;
    private final UserRepository userRepository;

    public EquipementVRController(EquipementVRService equipementVRService, UserRepository userRepository) {
        this.equipementVRService = equipementVRService;
        this.userRepository = userRepository;
    }

    private User getUser(UserDetailsImpl userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
    }

    @GetMapping
    public ResponseEntity<List<EquipementVRDTO>> getAll(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(equipementVRService.getByUser(getUser(userDetails).getId()));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody EquipementVRRequest dto) {
        try {
            return ResponseEntity.ok(equipementVRService.create(getUser(userDetails).getId(), dto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id,
            @RequestBody EquipementVRRequest dto) {
        try {
            return ResponseEntity.ok(equipementVRService.update(getUser(userDetails).getId(), id, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id) {
        try {
            equipementVRService.delete(getUser(userDetails).getId(), id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}