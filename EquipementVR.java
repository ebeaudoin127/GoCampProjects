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

    // ========= GETTERS =========
    public Long getId() { return id; }
    public User getUser() { return user; }
    public String getMarque() { return marque; }
    public String getModele() { return modele; }
    public Integer getLongueur() { return longueur; }
    public String getNoSerie() { return noSerie; }
    public String getNoPlaque() { return noPlaque; }
    public Boolean getHasExtension() { return hasExtension; }
    public Integer getExtensionConducteur() { return extensionConducteur; }
    public Integer getExtensionPassager() { return extensionPassager; }
    public Boolean getActif() { return actif; }

    // ========= SETTERS =========
    public void setId(Long id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setMarque(String marque) { this.marque = marque; }
    public void setModele(String modele) { this.modele = modele; }
    public void setLongueur(Integer longueur) { this.longueur = longueur; }
    public void setNoSerie(String noSerie) { this.noSerie = noSerie; }
    public void setNoPlaque(String noPlaque) { this.noPlaque = noPlaque; }
    public void setHasExtension(Boolean hasExtension) { this.hasExtension = hasExtension; }
    public void setExtensionConducteur(Integer extensionConducteur) { this.extensionConducteur = extensionConducteur; }
    public void setExtensionPassager(Integer extensionPassager) { this.extensionPassager = extensionPassager; }
    public void setActif(Boolean actif) { this.actif = actif; }
}
