package com.bumsoap.notes.cont;

import com.bumsoap.notes.dtos.UserDto;
import com.bumsoap.notes.models.User;
import com.bumsoap.notes.security.serv.UserDetailsImpl;
import com.bumsoap.notes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    UserService userService;

    @GetMapping("/getusers")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(),
                HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        Long loginId = ((UserDetailsImpl) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal()).getId();

        return new ResponseEntity<>(userService.getUserById(loginId, id),
                HttpStatus.OK);
    }

    @PutMapping("/update-role")
    public ResponseEntity<UserDto> updateUserRole(
            @RequestParam Long userId, @RequestParam String roleName) {
        UserDto userDto = userService.updateUserRole(userId, roleName);
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

}
