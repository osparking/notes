package com.bumsoap.notes.cont;

import java.lang.module.ResolutionException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.bumsoap.notes.models.AppRole;
import com.bumsoap.notes.models.Role;
import com.bumsoap.notes.models.User;
import com.bumsoap.notes.repo.RoleRepo;
import com.bumsoap.notes.repo.UserRepo;
import com.bumsoap.notes.requests.LoginRequest;
import com.bumsoap.notes.requests.SignUpRequest;
import com.bumsoap.notes.responses.LoginResponse;
import com.bumsoap.notes.responses.MessageResponse;
import com.bumsoap.notes.responses.UserInfoResponse;
import com.bumsoap.notes.security.jwt.JwtUtils;
import com.bumsoap.notes.security.serv.UserDetailsImpl;
import com.bumsoap.notes.service.TotpService;
import com.bumsoap.notes.service.UserService;
import com.bumsoap.notes.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthCon {
  private final AuthenticationManager authenticationManager;
  private final JwtUtils jwtUtils;
  private final UserRepo userRepo;
  private final PasswordEncoder encoder;
  private final RoleRepo roleRepo;
  private final UserService userService;
  private final AuthUtil authUtil;
  private final TotpService totpService;

  @PostMapping("/verify-2fa")
  public ResponseEntity<String> verify2FA(@RequestParam int code) {
    Long userId = authUtil.loggedInUserId();
    boolean validCode = userService.validate2FAcode(userId, code);

    if (validCode) {
      userService.enable2FA(userId);
      return ResponseEntity.ok("2FA 검증됨");
    } else {
      return ResponseEntity
          .status(HttpStatus.UNAUTHORIZED)
          .body("2FA 코드 오류");
    }
  }

  @PostMapping("/public/verify-2fa-login")
  public ResponseEntity<String> verify2FAlogin(@RequestParam int code,
                                               @RequestParam String jwt) {
    String username = jwtUtils.getUserNameFromJwtToken(jwt);
    User user = userService.findByUsername(username);

    if (userService.validate2FAcode(user.getUserId(), code)) {
      return ResponseEntity.ok("2FA 검증됨");
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body("2FA 코드 오류");
    }
  }

  @GetMapping("/user/2fa-status")
  public ResponseEntity<?> get2FAstatus() {
    User user = authUtil.loggedInUser();

    if (user == null) {
      return ResponseEntity
          .status(HttpStatus.NOT_FOUND).body("유저 부재");
    } else {
      return ResponseEntity.ok().body(
          Map.of("2FA-활성화됨", user.isTwoFactorEnabled()));
    }
  }

  @PostMapping("/enable-2fa")
  public ResponseEntity<String> enableUserFor2FA() {
    Long userId = authUtil.loggedInUserId();
    var secret = userService.generate2FAsecret(userId);
    String qrCodeUrl = totpService.getQRcodeUrl(secret,
        userService.getUserById(userId).getUsername());
    return ResponseEntity.ok(qrCodeUrl);
  }

  @PostMapping("/disable-2fa")
  public ResponseEntity<String> disableUserFor2FA() {
    Long userId = authUtil.loggedInUserId();
    userService.disable2FA(userId);
    return ResponseEntity.ok("2FA 비활성화됨");
  }

  @PostMapping("/public/reset-password")
  public ResponseEntity<?> resetPassword(@RequestParam String token,
                                         @RequestParam String newPassword) {
    try {
      userService.resetPassword(token, newPassword);
      return ResponseEntity.ok(new MessageResponse("패스워드 재설정 성공"));
    } catch (ResolutionException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new MessageResponse(e.getMessage()));
    }
  }

  @PostMapping("/public/forgotten-pwd")
  public ResponseEntity<?> forgottenPwd(@RequestParam String email) {
    try {
      userService.generatePasswordResetTokenFor(email);
      return ResponseEntity
          .ok(new MessageResponse("비밀번호 리셋 이메일 전송됨"));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new MessageResponse("비밀번호 리셋 이메일 오류"));
    }
  }

  @GetMapping("/username")
  public ResponseEntity<String> getUsername(
      @AuthenticationPrincipal UserDetails userDetails) {
    var name = userDetails == null ? "" : userDetails.getUsername();
    return ResponseEntity.ok().body(name);
  }

  @GetMapping("/user")
  public ResponseEntity<?> getUserDetails(
      @AuthenticationPrincipal UserDetails userDetails) {
    User user = userService.findByUsername(userDetails.getUsername());
    List<String> roles = userDetails.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());

    UserInfoResponse response = new UserInfoResponse(
        user.getUserId(),
        user.getUsername(),
        user.getEmail(),
        user.isAccountNonLocked(),
        user.isAccountNonExpired(),
        user.isCredentialsNonExpired(),
        user.isEnabled(),
        user.getCredentialsExpiration(),
        user.getAccountExpiration(),
        user.isTwoFactorEnabled(),
        roles,
        user.getSignUpMethod(),
        null
    );

    return ResponseEntity.ok().body(response);
  }

  @PostMapping("/public/signin")
  public ResponseEntity<?> authenticateUser(
      @RequestBody LoginRequest loginRequest) {
    Authentication authentication;

    try {
      authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              loginRequest.getUsername(), loginRequest.getPassword()));
    } catch (AuthenticationException exception) {
      Map<String, Object> map = new HashMap<>();
      map.put("status", false);
      User user = userService.findByUsername(loginRequest.getUsername());
      if (user.getSignUpMethod().equals("유저네임")) {
        map.put("message", "Bad credentials");
      } else {
        map.put("message", user.getSignUpMethod() + "로 로그인하세요.");
      }

      return new ResponseEntity<Object>(map, HttpStatus.BAD_REQUEST);
    }

    // set the authentication
    SecurityContextHolder.getContext().setAuthentication(authentication);

    var userDetails = (UserDetailsImpl) authentication.getPrincipal();
    userDetails.setLoginMethod("유저네임");
    String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

    // Collect roles from the UserDetails
    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

    // Prepare the response body, now including the JWT token directly in the body
    LoginResponse response =
        new LoginResponse(userDetails.getUsername(), roles, jwtToken);

    // Return the response entity with the JWT token included in the response body
    return ResponseEntity.ok(response);
  }

  @PostMapping("/public/signup")
  public ResponseEntity<?> registerUser(
      @RequestBody SignUpRequest signUpRequest) {
    if (userRepo.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.badRequest().body(
          new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepo.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest().body(
          new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user = new User(signUpRequest.getUsername(),
        signUpRequest.getEmail(),
        encoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRole();
    Role role;

    if (strRoles == null || strRoles.isEmpty()) {
      role = roleRepo.findByRoleName(AppRole.ROLE_USER)
          .orElseThrow(() ->
              new RuntimeException("Error: Role is not found."));
    } else {
      String roleStr = strRoles.iterator().next();
      if (roleStr.equals("admin")) {
        role = roleRepo.findByRoleName(AppRole.ROLE_ADMIN)
            .orElseThrow(() ->
                new RuntimeException("Error: Role is not found."));
      } else {
        role = roleRepo.findByRoleName(AppRole.ROLE_USER)
            .orElseThrow(() ->
                new RuntimeException("Error: Role is not found."));
      }

      user.setAccountNonLocked(true);
      user.setAccountNonExpired(true);
      user.setCredentialsNonExpired(true);
      user.setEnabled(true);
      user.setCredentialsExpiration(LocalDate.now().plusYears(1));
      user.setAccountExpiration(LocalDate.now().plusYears(1));
      user.setTwoFactorEnabled(false);
      user.setSignUpMethod("email");
    }
    user.setRole(role);
    userRepo.save(user);

    return ResponseEntity.ok(
        new MessageResponse("User registered successfully!"));
  }
}
