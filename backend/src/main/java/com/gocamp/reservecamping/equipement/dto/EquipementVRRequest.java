// ============================================================
// Fichier : EquipementVRRequest.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/equipement/dto
// Dernière modification : 2026-05-09
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - DTO de requête pour créer/modifier un équipement VR
// - Ajout des options de recherche par défaut
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

package com.gocamp.reservecamping.equipement.dto;

public class EquipementVRRequest {

    private String marque;
    private String modele;
    private Integer longueur;
    private String noSerie;
    private String noPlaque;
    private Boolean hasExtension;
    private Integer extensionConducteur;
    private Integer extensionPassager;
    private Boolean actif;

    private Boolean defaultRequiresWater;
    private Boolean defaultRequiresElectricity;
    private Boolean defaultRequiresSewer;
    private Boolean defaultRequires15_20Amp;
    private Boolean defaultRequires30Amp;
    private Boolean defaultRequires50Amp;

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
