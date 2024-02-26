package com.example.intentbi.exception;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidSortByPropertyException extends RuntimeException {

  private final List<String> invalidSortInfo;
  private final List<String> validProperties;

  public InvalidSortByPropertyException(
      List<String> invalidSortInfo, List<String> validProperties) {
    super();
    this.invalidSortInfo = invalidSortInfo;
    this.validProperties = validProperties;
  }

  public InvalidSortByPropertyException(
      List<String> invalidSortInfo, List<String> validProperties, String message) {
    super(message);
    this.invalidSortInfo = invalidSortInfo;
    this.validProperties = validProperties;
  }

  public InvalidSortByPropertyException(
      List<String> invalidSortInfo, List<String> validProperties, String message, Throwable cause) {
    super(message, cause);
    this.invalidSortInfo = invalidSortInfo;
    this.validProperties = validProperties;
  }

  public InvalidSortByPropertyException(
      List<String> invalidSortInfo, List<String> validProperties, Throwable cause) {
    super(cause);
    this.invalidSortInfo = invalidSortInfo;
    this.validProperties = validProperties;
  }

  protected InvalidSortByPropertyException(
      List<String> invalidSortInfo,
      List<String> validProperties,
      String message,
      Throwable cause,
      boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.invalidSortInfo = invalidSortInfo;
    this.validProperties = validProperties;
  }
}
