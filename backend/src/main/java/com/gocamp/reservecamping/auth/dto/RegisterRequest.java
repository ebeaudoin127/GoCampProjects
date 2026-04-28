// ============================================================
// Fichier : RegisterRequest.java
// Dernière modification : 2025-11-17
// Auteur : ChatGPT (corrections pour Eric Beaudoin)
// Résumé : ajout de countryId et provinceStateId pour rendre
//          pays et province/état obligatoires lors de l'inscription.
// ============================================================

package com.gocamp.reservecamping.auth.dto;

public record RegisterRequest(

        String firstname,
        String lastname,
        String email,
        String password,

        // 🔥 AJOUTÉ : pays obligatoire
        Long countryId,

        // 🔥 AJOUTÉ : province/état obligatoire
        Long provinceStateId

) {}
