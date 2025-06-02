package com.bumsoap.notes.service.impl;

import com.bumsoap.notes.service.TotpService;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

import java.nio.channels.spi.AbstractSelectionKey;

public class TotpServiceImpl implements TotpService {
  private final GoogleAuthenticator gAuthetor;

  public TotpServiceImpl(GoogleAuthenticator googleAuthenticator) {
    this.gAuthetor = googleAuthenticator;
  }

  public GoogleAuthenticatorKey generateSecret() {
    return gAuthetor.createCredentials();
  }

  public String getQRcodeUrl(
      GoogleAuthenticatorKey secret, String username) {
    return GoogleAuthenticatorQRGenerator.getOtpAuthURL(
        "비밀 노트 응용", username, secret);
  }

  public boolean verifyCode(String secret, int code) {
    return gAuthetor.authorize(secret, code);
  }
}
