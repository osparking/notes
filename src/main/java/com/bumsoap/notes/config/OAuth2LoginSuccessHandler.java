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
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

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

}