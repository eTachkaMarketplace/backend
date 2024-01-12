package com.sellbycar.marketplace.user;

import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


@Data
public class UserDetailsImpl implements UserDetails {

    private final UserDAO user;

    @Override
    public Collection<UserAuthority> getAuthorities() {
        return user.getAuthorities();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    }
}
