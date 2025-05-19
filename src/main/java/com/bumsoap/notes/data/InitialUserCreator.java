package com.bumsoap.notes.data;

import com.bumsoap.notes.models.AppRole;
import com.bumsoap.notes.models.Role;
import com.bumsoap.notes.models.User;
import com.bumsoap.notes.repo.RoleRepo;
import com.bumsoap.notes.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class InitialUserCreator implements ApplicationListener<ApplicationReadyEvent> {
  private final RoleRepo roleRepo;
  private final UserRepo userRepo;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    createAdmin();
    createUser1();
    create20Users();
  }

  private void createAdmin() {
    Role adminRole = roleRepo.findByRoleName(AppRole.ROLE_ADMIN)
        .orElseGet(() -> roleRepo.save(new Role(AppRole.ROLE_ADMIN)));

    if (!userRepo.existsByUsername("admin")) {
      User admin = new User("admin", "admin@email.com", passwordEncoder.encode("1234"));
      admin.setAccountNonLocked(true);
      admin.setAccountNonExpired(true);
      admin.setCredentialsNonExpired(true);
      admin.setEnabled(true);
      admin.setCredentialsExpiration(LocalDate.now().plusYears(1));
      admin.setAccountExpiration(LocalDate.now().plusYears(1));
      admin.setTwoFactorEnabled(false);
      admin.setSignUpMethod("email");
      admin.setRole(adminRole);
      userRepo.save(admin);
    }
  }

  private void createUser1() {
    Role userRole = roleRepo.findByRoleName(AppRole.ROLE_USER)
        .orElseGet(() -> roleRepo.save(new Role(AppRole.ROLE_USER)));

    if (!userRepo.existsByUsername("user1")) {
      User user1 = new User("user1", "user1@email.com", passwordEncoder.encode("1234"));
      user1.setAccountNonLocked(false);
      user1.setAccountNonExpired(true);
      user1.setCredentialsNonExpired(true);
      user1.setEnabled(true);
      user1.setCredentialsExpiration(LocalDate.now().plusYears(1));
      user1.setAccountExpiration(LocalDate.now().plusYears(1));
      user1.setTwoFactorEnabled(false);
      user1.setSignUpMethod("email");
      user1.setRole(userRole);
      userRepo.save(user1);
    }
  }

  private void create20Users() {
    Role userRole = roleRepo.findByRoleName(AppRole.ROLE_USER)
        .orElseGet(() -> roleRepo.save(new Role(AppRole.ROLE_USER)));

    for (int i = 0; i < 20; i++) {
      String username = "user" + (i + 2);
      if (!userRepo.existsByUsername(username)) {
        User user = new User(username, username + "@email.com", passwordEncoder.encode("1234"));
        user.setAccountNonLocked(false);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setCredentialsExpiration(LocalDate.now().plusYears(1));
        user.setAccountExpiration(LocalDate.now().plusYears(1));
        user.setTwoFactorEnabled(false);
        user.setSignUpMethod("email");
        user.setRole(userRole);
        userRepo.save(user);
      }
    }
  }
}
