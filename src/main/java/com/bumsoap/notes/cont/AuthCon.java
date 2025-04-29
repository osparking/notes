package com.bumsoap.notes.cont;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthCon {
  @DeleteMapping("/public/message")
  public String message(){
    return "이것은 공공 메시지입니다.";
  }
}
