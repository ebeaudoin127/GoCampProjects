

// ============================================================
// Fichier : GlobalExceptionHandler.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/exceptions
// Dernière modification : 2026-05-06
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Gestion centralisée des erreurs REST
// - Transforme les RuntimeException en réponses JSON propres
// - Transforme les erreurs générales en réponses JSON propres
//
// Historique des modifications :
// 2025-11-17
// - Création/correction initiale du gestionnaire global
// - Gestion RuntimeException en 400 Bad Request
// - Gestion générique Exception en 500 Internal Server Error
//
// 2026-05-06
// - Conservation du package existant com.gocamp.reservecamping.exceptions
// - Correction du conflit avec common.exception.GlobalExceptionHandler
// - Uniformisation du JSON retourné avec success, message, path, timestamp
// ============================================================

package com.gocamp.reservecamping.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntime(
            RuntimeException ex,
            HttpServletRequest request
    ) {

        return ResponseEntity.badRequest().body(
                Map.of(
                        "success", false,
                        "message", ex.getMessage(),
                        "path", request.getRequestURI(),
                        "timestamp", LocalDateTime.now().toString()
                )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(
            Exception ex,
            HttpServletRequest request
    ) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of(
                        "success", false,
                        "message", "Erreur interne du serveur",
                        "details", ex.getMessage(),
                        "path", request.getRequestURI(),
                        "timestamp", LocalDateTime.now().toString()
                )
        );
    }
}