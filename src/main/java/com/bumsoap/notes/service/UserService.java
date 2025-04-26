package com.bumsoap.notes.service;

import com.bumsoap.notes.dtos.UserDto;
import com.bumsoap.notes.models.User;

import java.util.List;

public interface UserService {
    void updateUserRole(Long userId, String roleName);
    List<User> getAllUsers();
    UserDto getUserById(Long id);
}
