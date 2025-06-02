package com.bumsoap.notes.service.impl;

import com.bumsoap.notes.service.TotpService;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

public class TotpServiceImpl implements TotpService {
  private final GoogleAuthenticator gAuthetor;

  public TotpServiceImpl(GoogleAuthenticator googleAuthenticator) {
    this.gAuthetor = googleAuthenticator;
  }

  public GoogleAuthenticatorKey generateSecret() {
    return gAuthetor.createCredentials();
  }
}
