package com.example.intentbi.model;

public enum Authority {
  USER("USER");

  Authority(String name) {}

  public static Authority fromStr(String str) {
    for (Authority authority : Authority.values()) {
      if (str.equals(authority.name())) {
        return authority;
      }
    }

    throw new IllegalArgumentException();
  }
}
