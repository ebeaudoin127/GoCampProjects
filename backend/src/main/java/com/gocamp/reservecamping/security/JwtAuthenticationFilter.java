

// ============================================================
// Fichier : JwtAuthenticationFilter.java
// Chemin : backend/src/main/java/com/gocamp/reservecamping/security
// Dernière modification : 2026-04-18
//
// Résumé :
// - Filtre JWT principal
// - Corrige le problème de login (/api/auth/me)
// - Autorise l’accès public aux fichiers /uploads/**
// ============================================================

package com.gocamp.reservecamping.security;

import com.gocamp.reservecamping.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService,
                                   CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    // ============================================================
    // Routes publiques ignorées par le filtre JWT
    // ============================================================
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        return pathMatcher.match("/uploads/**", path)
                || pathMatcher.match("/api/auth/login", path)
                || pathMatcher.match("/api/auth/register", path)
                || pathMatcher.match("/api/countries/**", path)
                || pathMatcher.match("/api/province-states/**", path)
                || pathMatcher.match("/logos/**", path)
                || pathMatcher.match("/backgrounds/**", path)
                || "/error".equals(path)
                || "/favicon.ico".equals(path);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // Pas de Bearer → on continue
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(7);
            String email = jwtService.extractEmail(token);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetailsImpl userDetails =
                        (UserDetailsImpl) userDetailsService.loadUserByUsername(email);

                // Vérification : le token est valide + correspond à cet utilisateur
                if (jwtService.isTokenValid(token, userDetails)) {

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

        } catch (Exception ex) {
            // Token invalide, expiré, manipulé...
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Token invalide ou expiré\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }
}