// ============================================================
// Fichier : ApiErrorResponse.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/common/dto
// Dernière modification : 2026-05-06
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - DTO standard pour retourner les erreurs API au frontend
// - Utilisé par GlobalExceptionHandler
//
// Historique des modifications :
// 2026-05-06
// - Création initiale du DTO
// ============================================================

package com.gocamp.reservecamping.common.dto;

import java.time.LocalDateTime;

public class ApiErrorResponse {

    private boolean success;
    private String message;
    private String path;
    private LocalDateTime timestamp;

    public ApiErrorResponse() {
    }

    public ApiErrorResponse(
            boolean success,
            String message,
            String path,
            LocalDateTime timestamp
    ) {
        this.success = success;
        this.message = message;
        this.path = path;
        this.timestamp = timestamp;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(
            boolean success
    ) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(
            String message
    ) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(
            String path
    ) {
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(
            LocalDateTime timestamp
    ) {
        this.timestamp = timestamp;
    }
}