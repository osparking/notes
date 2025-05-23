package com.bumsoap.notes.service;

import com.bumsoap.notes.dtos.UserDto;
import com.bumsoap.notes.models.Role;
import com.bumsoap.notes.models.User;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface UserService {
  void generatePasswordResetTokenFor(String email);

  void updatePassword(Long userId, String password);

  void updateCredentialsExpiryStatus(
      Long userId, boolean expire);

  void updateAccountEnabledStatus(Long userId, boolean enabled);

  void updateAccountExpiryStatus(Long userId, boolean expire);

  List<Role> getAllRoles();

  void updateAccountLockStatus(Long userId, boolean lock);

  User findByUsername(String username);

  //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    UserDto updateUserRole(Long userId, String roleName);

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    List<User> getAllUsers();

//    @PreAuthorize("hasRole('ROLE_ADMIN') OR #loginId == #userId")
    UserDto getUserById(Long loginId, Long userId);
}
