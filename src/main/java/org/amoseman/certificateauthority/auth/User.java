package org.amoseman.certificateauthority.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class User implements UserDetails {
    private String username;
    private boolean isExpired;
    private String[] roles;

    protected User(String username, boolean isExpired, String... roles) {
        this.username = username;
        this.isExpired = isExpired;
        this.roles = roles;
    }

    public boolean hasRole(String targetRole) {
        for (String role : roles) {
            if (role.equals(targetRole)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !isExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !isExpired;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
