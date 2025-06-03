package com.bumsoap.notes.config;

import com.bumsoap.notes.models.AppRole;
import com.bumsoap.notes.models.Role;
import com.bumsoap.notes.models.User;
import com.bumsoap.notes.repo.RoleRepo;
import com.bumsoap.notes.security.jwt.JwtUtils;
import com.bumsoap.notes.security.serv.UserDetailsImpl;
import com.bumsoap.notes.service.UserService;
import com.bumsoap.notes.util.SLogin;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler
    extends SavedRequestAwareAuthenticationSuccessHandler {

  @Autowired
  private final UserService userService;

  @Autowired
  private final JwtUtils jwtUtils;

  @Autowired
  RoleRepo roleRepository;

  @Value("${frontend.url}")
  private String frontendUrl;

  String username;
  String idAttributeKey;
  String signUpMethod = null;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication)
      throws ServletException, IOException {
    OAuth2AuthenticationToken oAuth2AuthenticationToken
        = (OAuth2AuthenticationToken) authentication;
    String oAuth2 = oAuth2AuthenticationToken
        .getAuthorizedClientRegistrationId();
    SLogin slogin = SLogin.valueOf(oAuth2.toUpperCase());
    var oauth2User = (DefaultOAuth2User) authentication.getPrincipal();
    Map<String, Object> attributes = oauth2User.getAttributes();

    if (slogin == SLogin.GITHUB || slogin == SLogin.GOOGLE) {
      String email = attributes.getOrDefault("email", "").toString();
      String name = attributes.getOrDefault("name", "").toString();

      if (slogin == SLogin.GITHUB) {
        username = attributes.getOrDefault("login", "").toString();
        idAttributeKey = "id";
      } else {
        username = email.split("@")[0];
        idAttributeKey = "sub";
      }
      System.out.println("OAUTH: " + email + " : " + name + " : " + username);

      userService.findByEmail(email)
          .ifPresentOrElse(
              // 이메일이 DB 에 존재하는 경우 처리
              user -> {
                putAuth2Context(user.getRole().getRoleName().name(),
                    attributes, idAttributeKey, oAuth2);
                username = user.getUsername();
              },
              // 이메일이 DB 에 부재인 경우 처리
              () -> {
                User newUser = new User();
                Optional<Role> userRole = roleRepository
                    .findByRoleName(AppRole.ROLE_USER);

                // Fetch existing role
                if (userRole.isPresent()) {
                  newUser.setRole(userRole.get()); // Set existing role
                  newUser.setEmail(email);
                  newUser.setUsername(username);
                  newUser.setCredentialsExpiration(LocalDate.now().plusYears(1));
                  newUser.setAccountExpiration(LocalDate.now().plusYears(1));
                  newUser.setSignUpMethod(oAuth2);
                  userService.registerUser(newUser);
                  putAuth2Context(userRole.get().getRoleName().name(),
                      attributes, idAttributeKey, oAuth2);
                } else {
                  // Handle the case where the role is not found
                  throw new RuntimeException("기본 롤이 DB 에 없음!");
                }
              }
          );
    }
    this.setAlwaysUseDefaultTargetUrl(true);

    // JWT TOKEN LOGIC
    // Extract necessary attributes
    String email = (String) attributes.get("email");
    System.out.println("SuccessHandler: " + username + " : " + email);

    Set<SimpleGrantedAuthority> authorities=
        oauth2User.getAuthorities().stream().map(
            authority -> new SimpleGrantedAuthority(
                authority.getAuthority())).collect(Collectors.toSet());
    User user = userService.findByEmail(email).orElseThrow(
        () -> new RuntimeException("이메일로 유저 검색 실패"));
    authorities.add(new SimpleGrantedAuthority(
        user.getRole().getRoleName().name()));

    // Create UserDetailsImpl instance
    UserDetailsImpl userDetails = new UserDetailsImpl(
        null,
        username,
        email,
        null,
        false,
        authorities
    );

    // Generate JWT token
    String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

    // Redirect to the frontend with the JWT token
    String targetUrl = UriComponentsBuilder.fromUriString(
            frontendUrl + "/oauth2/redirect")
        .queryParam("token", jwtToken)
        .build().toUriString();
    this.setDefaultTargetUrl(targetUrl);
    super.onAuthenticationSuccess(request, response, authentication);
  }

  private void putAuth2Context(String role,
                               Map<String, Object> attributes,
                               String idAttributeKey,
                               String oAuth2) {
    DefaultOAuth2User oauthUser = new DefaultOAuth2User(
        List.of(new SimpleGrantedAuthority(role)),
        attributes,
        idAttributeKey
    );
    Authentication securityAuth = new OAuth2AuthenticationToken(
        oauthUser,
        List.of(new SimpleGrantedAuthority(role)),
        oAuth2
    );
    SecurityContextHolder.getContext()
        .setAuthentication(securityAuth);
  }
}