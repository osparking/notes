package com.bumsoap.notes.config;

import com.bumsoap.notes.repo.RoleRepo;
import com.bumsoap.notes.security.jwt.JwtUtils;
import com.bumsoap.notes.service.UserService;
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

import java.io.IOException;
import java.util.List;
import java.util.Map;

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

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication)
      throws ServletException, IOException
  {}

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