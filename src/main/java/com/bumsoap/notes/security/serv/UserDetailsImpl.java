package com.bumsoap.notes.security.serv;

import com.bumsoap.notes.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@Data
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String email;
    @JsonIgnore
    private String password;
    private boolean is2faEnabled;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String username, String email,
                           String password, boolean is2faEnabled,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.is2faEnabled = is2faEnabled;
        this.authorities = authorities;
    }
    public static UserDetailsImpl build(User user) {
        var authority = new SimpleGrantedAuthority(
                user.getRole().getRoleName().name());
        return new UserDetailsImpl(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.isTwoFactorEnabled(),
                List.of(authority) // Add logic for authorities if applicable
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public boolean isTwoFactorEnabled() {
        return is2faEnabled;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
        return true;
    }

    public boolean is2faEnabled() {
        return is2faEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsImpl that = (UserDetailsImpl) o;
        return username.equals(that.username);
    }
}
