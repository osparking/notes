package com.bumsoap.notes.service.impl;

import com.bumsoap.notes.service.TotpService;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import org.springframework.stereotype.Service;

@Service
public class TotpServiceImpl implements TotpService {
  private final GoogleAuthenticator gAuthetor;

  public TotpServiceImpl(GoogleAuthenticator googleAuthenticator) {
    this.gAuthetor = googleAuthenticator;
  }

  public TotpServiceImpl() {
    this.gAuthetor = new GoogleAuthenticator();
  }

  @Override
  public GoogleAuthenticatorKey generateSecret() {
    return gAuthetor.createCredentials();
  }

  @Override
  public String getQRcodeUrl(
      GoogleAuthenticatorKey secret, String username) {
    return GoogleAuthenticatorQRGenerator.getOtpAuthURL(
        "비밀 노트 응용", username, secret);
  }

  @Override
  public boolean verifyCode(String secret, int code) {
    return gAuthetor.authorize(secret, code);
  }
}
