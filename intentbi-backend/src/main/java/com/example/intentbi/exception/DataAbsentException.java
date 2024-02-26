package com.example.intentbi.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataAbsentException extends RuntimeException {

  private final long id;

  public DataAbsentException(long id) {
    super();
    this.id = id;
  }

  public DataAbsentException(long id, String message) {
    super(message);
    this.id = id;
  }

  public DataAbsentException(long id, String message, Throwable cause) {
    super(message, cause);
    this.id = id;
  }

  public DataAbsentException(long id, Throwable cause) {
    super(cause);
    this.id = id;
  }

  protected DataAbsentException(
      long id,
      String message,
      Throwable cause,
      boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.id = id;
  }
}
