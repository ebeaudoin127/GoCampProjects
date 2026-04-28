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

    // GETTERS
    public String getMarque() { return marque; }
    public String getModele() { return modele; }
    public Integer getLongueur() { return longueur; }
    public String getNoSerie() { return noSerie; }
    public String getNoPlaque() { return noPlaque; }
    public Boolean getHasExtension() { return hasExtension; }
    public Integer getExtensionConducteur() { return extensionConducteur; }
    public Integer getExtensionPassager() { return extensionPassager; }
    public Boolean getActif() { return actif; }

    // SETTERS
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