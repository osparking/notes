package com.bumsoap.notes.service.impl;

import com.bumsoap.notes.service.TotpService;
import com.warrenstrange.googleauth.GoogleAuthenticator;

public class TotpServiceImpl implements TotpService {
  private final GoogleAuthenticator gAuthetor;

  public TotpServiceImpl(GoogleAuthenticator googleAuthenticator) {
    this.gAuthetor = googleAuthenticator;
  }
}
