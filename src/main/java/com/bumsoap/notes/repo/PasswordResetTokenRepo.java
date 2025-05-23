package com.bumsoap.notes.repo;

import com.bumsoap.notes.models.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepo
    extends JpaRepository<PasswordResetToken, Long> {
  PasswordResetToken findByToken(String token);
}
