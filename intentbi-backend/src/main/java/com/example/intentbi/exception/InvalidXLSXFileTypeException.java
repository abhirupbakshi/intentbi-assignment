package com.example.intentbi.exception;

public class InvalidXLSXFileTypeException extends RuntimeException {

  public InvalidXLSXFileTypeException() {
    super();
  }

  public InvalidXLSXFileTypeException(String message) {
    super(message);
  }

  public InvalidXLSXFileTypeException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidXLSXFileTypeException(Throwable cause) {
    super(cause);
  }

  protected InvalidXLSXFileTypeException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
