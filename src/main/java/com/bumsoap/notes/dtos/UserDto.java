package com.bumsoap.notes.dtos;

import com.bumsoap.notes.models.Role;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long userId;
    private String username;
    private String email;
    private boolean accountNonLocked;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
    private boolean enabled;

    private LocalDate credentialsExpiration;
    private LocalDate accountExpiration;

    private String twoFactorSecret;
    private boolean isTwoFactorEnabled;
    private String signUpMethod; // google, naver, etc.

    private Role role;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
