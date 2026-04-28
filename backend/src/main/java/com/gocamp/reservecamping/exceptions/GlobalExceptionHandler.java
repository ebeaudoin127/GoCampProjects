// ============================================================
// Fichier : GlobalExceptionHandler.java
// Dernière modification : 2025-11-17
// Auteur : ChatGPT (corrigé pour Eric Beaudoin)
// Résumé : Gestion centralisée des erreurs REST. Transforme les
//          RuntimeException en réponses JSON propres.
// ============================================================

package com.gocamp.reservecamping.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // ---------------------------------------------------------
    // Gestion des RuntimeException : renvoie 400 Bad Request
    // ---------------------------------------------------------
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntime(RuntimeException ex) {

        return ResponseEntity.badRequest().body(
                Map.of(
                        "timestamp", LocalDateTime.now().toString(),
                        "status", 400,
                        "error", ex.getMessage()
                )
        );
    }

    // ---------------------------------------------------------
    // Gestion générique de toutes les autres erreurs
    // ---------------------------------------------------------
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of(
                        "timestamp", LocalDateTime.now().toString(),
                        "status", 500,
                        "error", "Erreur interne du serveur",
                        "details", ex.getMessage()
                )
        );
    }
}
