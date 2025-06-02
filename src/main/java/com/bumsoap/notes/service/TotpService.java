package com.bumsoap.notes.service;

import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

public interface TotpService {
  GoogleAuthenticatorKey generateSecret();

  String getQRcodeUrl(
      GoogleAuthenticatorKey secret, String username);

  boolean verifyCode(String secret, int code);
}
