package com.bumsoap.notes.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {
  @Value("${frontend.url}")
  private String frontendUrl;
}
