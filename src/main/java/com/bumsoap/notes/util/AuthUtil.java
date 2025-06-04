package com.bumsoap.notes.util;

import com.bumsoap.notes.models.User;
import com.bumsoap.notes.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {
  @Autowired
  UserRepo userRepo;

  public Long loggedInUserId() {
    var auth = SecurityContextHolder.getContext().getAuthentication();
    User user = userRepo.findByUsername(auth.getName())
        .orElseThrow(() ->
            new RuntimeException("유저 이름 부재: " + auth.getName()));
    return user.getUserId();
  }

}
