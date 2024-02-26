package com.example.intentbi.exception;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidXLSXHeaderException extends RuntimeException {

  private final List<String> invalidHeaders;
  private final List<String> validHeaders;

  public InvalidXLSXHeaderException(List<String> invalidHeaders, List<String> validHeaders) {
    super();
    this.invalidHeaders = invalidHeaders;
    this.validHeaders = validHeaders;
  }

  public InvalidXLSXHeaderException(
      List<String> invalidHeaders, List<String> validHeaders, String message) {
    super(message);
    this.invalidHeaders = invalidHeaders;
    this.validHeaders = validHeaders;
  }

  public InvalidXLSXHeaderException(
      List<String> invalidHeaders, List<String> validHeaders, String message, Throwable cause) {
    super(message, cause);
    this.invalidHeaders = invalidHeaders;
    this.validHeaders = validHeaders;
  }

  public InvalidXLSXHeaderException(
      List<String> invalidHeaders, List<String> validHeaders, Throwable cause) {
    super(cause);
    this.invalidHeaders = invalidHeaders;
    this.validHeaders = validHeaders;
  }

  protected InvalidXLSXHeaderException(
      List<String> invalidHeaders,
      List<String> validHeaders,
      String message,
      Throwable cause,
      boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.invalidHeaders = invalidHeaders;
    this.validHeaders = validHeaders;
  }
}
