package com.example.intentbi.model;

public enum DiscountBand {
  NONE,
  LOW,
  MEDIUM,
  HIGH;

  public static DiscountBand fromStr(String str) {
    for (DiscountBand db : DiscountBand.values()) {
      if (str.equalsIgnoreCase(db.name())) {
        return db;
      }
    }

    throw new IllegalArgumentException();
  }
}
