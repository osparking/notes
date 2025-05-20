package com.bumsoap.notes.service.impl;

import com.bumsoap.notes.dtos.UserDto;
import com.bumsoap.notes.models.AppRole;
import com.bumsoap.notes.models.Role;
import com.bumsoap.notes.models.User;
import com.bumsoap.notes.repo.RoleRepo;
import com.bumsoap.notes.repo.UserRepo;
import com.bumsoap.notes.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
  private final UserRepo userRepo;
  private final RoleRepo roleRepo;

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
