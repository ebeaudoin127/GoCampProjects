package com.gocamp.reservecamping.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gocamp.reservecamping.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {

    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    // ⚠️ Exposition contrôlée de l’entité pour ton AuthController
    @JsonIgnore
    public User getUserEntity() {
        return user;
    }

    // ========================================
    //           AUTHORITIES (ROLES)
    // ========================================
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        String roleName = user.getRole().getName();

        // ★ Spring Security exige "ROLE_XXXX"
        return List.of((GrantedAuthority) () -> "ROLE_" + roleName);
    }

    // ========================================
    //           LOGIN / PASSWORD
    // ========================================
    @Override
    @JsonIgnore
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    // ========================================
    //           ACCOUNT FLAGS
    // ========================================
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
