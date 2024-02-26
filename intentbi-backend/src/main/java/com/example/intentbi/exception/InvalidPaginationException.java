package com.example.intentbi.exception;

public class InvalidPaginationException extends RuntimeException {

  public InvalidPaginationException() {
    super();
  }

  public InvalidPaginationException(String message) {
    super(message);
  }

  public InvalidPaginationException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidPaginationException(Throwable cause) {
    super(cause);
  }

  protected InvalidPaginationException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
