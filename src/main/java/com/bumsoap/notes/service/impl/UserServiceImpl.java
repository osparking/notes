package com.bumsoap.notes.service.impl;

import com.bumsoap.notes.dtos.UserDto;
import com.bumsoap.notes.models.AppRole;
import com.bumsoap.notes.models.PasswordResetToken;
import com.bumsoap.notes.models.Role;
import com.bumsoap.notes.models.User;
import com.bumsoap.notes.repo.PasswordResetTokenRepo;
import com.bumsoap.notes.repo.RoleRepo;
import com.bumsoap.notes.repo.UserRepo;
import com.bumsoap.notes.service.UserService;
import com.bumsoap.notes.util.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
  private final UserRepo userRepo;
  private final RoleRepo roleRepo;
  private final PasswordEncoder passwordEncoder;
  private final PasswordResetTokenRepo passwordResetTokenRepo;
  private final EmailService emailService;

  @Value("${frontend.url}")
  private String frontendUrl;

  @Override
  public void generatePasswordResetTokenFor(String email) {
    User user = userRepo.findByEmail(email).orElseThrow(
        () -> new RuntimeException("발견되지 않은 이메일: " + email));
    String token = UUID.randomUUID().toString();
    Instant expiryDate = Instant.now().plus(24, ChronoUnit.HOURS);
    PasswordResetToken resetToken =
        new PasswordResetToken(token, expiryDate, user);
    passwordResetTokenRepo.save(resetToken);

    String pwdResetLink =
        frontendUrl + "/reset-password?token=" + token;
    /**
     * 위 링크를 내포한 패스워드 변경 안내 이메일을 전송한다.
     */
    emailService.sendPasswordResetMail(user.getEmail(), pwdResetLink);
  }

  @Override
  public void updatePassword(Long userId, String password) {
    try {
      User user = userRepo.findById(userId)
          .orElseThrow(() -> new RuntimeException("유저 발견 실패"));
      user.setPassword(passwordEncoder.encode(password));
      userRepo.save(user);
    } catch (Exception e) {
      throw new RuntimeException("패스워드 갱신 실패!");
    }
  }

  @Override
  public void updateCredentialsExpiryStatus(
      Long userId, boolean expire) {
    User user = userRepo.findById(userId).orElseThrow(()
        -> new RuntimeException("유저 발견 실패"));
    user.setCredentialsNonExpired(!expire);
    userRepo.save(user);
  }

  @Override
  public void updateAccountEnabledStatus(Long userId, boolean enabled) {
    User user = userRepo.findById(userId).orElseThrow(()
        -> new RuntimeException("유저 발견 실패"));
    user.setEnabled(enabled);
    userRepo.save(user);
  }

  @Override
  public void updateAccountExpiryStatus(Long userId, boolean expire) {
    User user = userRepo.findById(userId).orElseThrow(()
        -> new RuntimeException("유저 발견 실패"));
    user.setAccountNonExpired(!expire);
    userRepo.save(user);
  }

  @Override
  public List<Role> getAllRoles() {
    return roleRepo.findAll();
  }

  @Override
  public void updateAccountLockStatus(Long userId, boolean lock) {
    User user = userRepo.findById(userId).orElseThrow(()
        -> new RuntimeException("유저 발견 실패"));
    user.setAccountNonLocked(!lock);
    userRepo.save(user);
  }

  @Override
  public User findByUsername(String username) {
    Optional<User> user = userRepo.findByUsername(username);
    return user.orElseThrow(() -> new RuntimeException(
        "User not found with username: " + username));
  }

  @Override
  public UserDto updateUserRole(Long userId, String roleName) {
    User user = userRepo.findById(userId).orElseThrow(
        () -> new RuntimeException("유저 발견 실패"));
    AppRole appRole = AppRole.valueOf(roleName);
    Role role = roleRepo.findByRoleName(appRole).orElseThrow(
        () -> new RuntimeException("롤 발견 실패"));
    user.setRole(role);
    return convertToDto(userRepo.save(user));
  }

  @Override
  public List<User> getAllUsers() {
    return userRepo.findAll();
  }

  @Override
  public UserDto getUserById(Long loginId, Long userId) {
    User user = userRepo.findById(userId).orElseThrow();
    return convertToDto(user);
  }

  private UserDto convertToDto(User user) {
    return new UserDto(
        user.getUserId(),
        user.getUsername(),
        user.getEmail(),
        user.isAccountNonLocked(),
        user.isAccountNonExpired(),
        user.isCredentialsNonExpired(),
        user.isEnabled(),

        user.getCredentialsExpiration(),
        user.getAccountExpiration(),

        user.getTwoFactorSecret(),
        user.isTwoFactorEnabled(),
        user.getSignUpMethod(),

        user.getRole(),
        user.getCreatedDate(),
        user.getLastModifiedDate()
    );
  }
}
