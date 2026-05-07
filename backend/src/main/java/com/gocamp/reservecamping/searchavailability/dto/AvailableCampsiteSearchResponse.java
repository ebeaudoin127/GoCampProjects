// ============================================================
// Fichier : AvailableCampsiteSearchResponse.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/searchavailability/dto
// Dernière modification : 2026-05-07
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - DTO de réponse pour la recherche de terrains disponibles
// - Contient la liste des terrains trouvés et les infos pagination
//
// Historique des modifications :
// 2026-05-07
// - Création initiale du DTO
// - Ajout results
// - Ajout totalResults / page / size
// ============================================================

package com.gocamp.reservecamping.searchavailability.dto;

import java.util.ArrayList;
import java.util.List;

public class AvailableCampsiteSearchResponse {

    private List<AvailableCampsiteDto> results = new ArrayList<>();

    private int totalResults;
    private int page;
    private int size;

    public List<AvailableCampsiteDto> getResults() {
        return results;
    }

    public void setResults(List<AvailableCampsiteDto> results) {
        this.results = results;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}

