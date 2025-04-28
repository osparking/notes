package com.bumsoap.notes.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class RequestValidationFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain)
      throws ServletException, IOException {
    String header = request.getHeader("X-Valid-Request");
    if (header == null || !header.equals("true")) {
      response.sendError(
          HttpServletResponse.SC_BAD_REQUEST, "잘못된 요청");
      return;
    }
    filterChain.doFilter(request, response);
  }
}
