package com.example.intentbi.exception;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidSortDirectionException extends RuntimeException {

  private final List<String> forProperties;

  public InvalidSortDirectionException(List<String> forProperties) {
    super();
    this.forProperties = forProperties;
  }

  public InvalidSortDirectionException(List<String> forProperties, String message) {
    super(message);
    this.forProperties = forProperties;
  }

  public InvalidSortDirectionException(
      List<String> forProperties, String message, Throwable cause) {
    super(message, cause);
    this.forProperties = forProperties;
  }

  public InvalidSortDirectionException(List<String> forProperties, Throwable cause) {
    super(cause);
    this.forProperties = forProperties;
  }

  protected InvalidSortDirectionException(
      List<String> forProperties,
      String message,
      Throwable cause,
      boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.forProperties = forProperties;
  }
}
