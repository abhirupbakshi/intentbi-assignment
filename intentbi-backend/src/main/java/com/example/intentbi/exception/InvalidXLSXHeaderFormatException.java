package com.example.intentbi.exception;

public class InvalidXLSXHeaderFormatException extends RuntimeException {

  public InvalidXLSXHeaderFormatException() {
    super();
  }

  public InvalidXLSXHeaderFormatException(String message) {
    super(message);
  }

  public InvalidXLSXHeaderFormatException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidXLSXHeaderFormatException(Throwable cause) {
    super(cause);
  }

  protected InvalidXLSXHeaderFormatException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
