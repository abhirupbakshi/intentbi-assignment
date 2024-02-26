package com.example.intentbi.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserExistsException extends RuntimeException {

  private final String username;

  public UserExistsException(String username) {
    super();
    this.username = username;
  }

  public UserExistsException(String username, String message) {
    super(message);
    this.username = username;
  }

  public UserExistsException(String username, String message, Throwable cause) {
    super(message, cause);
    this.username = username;
  }

  public UserExistsException(String username, Throwable cause) {
    super(cause);
    this.username = username;
  }

  protected UserExistsException(
      String username,
      String message,
      Throwable cause,
      boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.username = username;
  }
}
