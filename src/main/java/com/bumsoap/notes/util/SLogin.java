package com.bumsoap.notes.util;

public enum SLogin {
  GOOGLE("구글"),
  GITHUB("깃허브");

  private final String label;

  SLogin(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }
}
