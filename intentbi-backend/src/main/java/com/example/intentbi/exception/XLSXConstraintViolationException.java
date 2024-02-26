package com.example.intentbi.exception;

import jakarta.validation.ConstraintViolation;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class XLSXConstraintViolationException extends RuntimeException {

  private final Map<Integer, List<ConstraintViolation<Object>>> violations;
  private final Class<?> ofType;

  public XLSXConstraintViolationException(
      Class<?> ofType, Map<Integer, List<ConstraintViolation<Object>>> violations) {
    super();
    this.violations = violations;
    this.ofType = ofType;
  }

  public XLSXConstraintViolationException(
      Class<?> ofType, Map<Integer, List<ConstraintViolation<Object>>> violations, String message) {
    super(message);
    this.violations = violations;
    this.ofType = ofType;
  }

  public XLSXConstraintViolationException(
      Class<?> ofType,
      Map<Integer, List<ConstraintViolation<Object>>> violations,
      String message,
      Throwable cause) {
    super(message, cause);
    this.violations = violations;
    this.ofType = ofType;
  }

  public XLSXConstraintViolationException(
      Class<?> ofType,
      Map<Integer, List<ConstraintViolation<Object>>> violations,
      Throwable cause) {
    super(cause);
    this.violations = violations;
    this.ofType = ofType;
  }

  protected XLSXConstraintViolationException(
      Class<?> ofType,
      Map<Integer, List<ConstraintViolation<Object>>> violations,
      String message,
      Throwable cause,
      boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.violations = violations;
    this.ofType = ofType;
  }
}
