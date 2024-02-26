package com.example.intentbi.exception;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.FieldError;

@Getter
@Setter
public class ConstraintViolationException extends RuntimeException {

  private final List<FieldError> violations;
  private final Class<?> ofType;

  public ConstraintViolationException(Class<?> ofType, List<FieldError> violations) {
    super();
    this.violations = violations;
    this.ofType = ofType;
  }

  public ConstraintViolationException(
      Class<?> ofType, List<FieldError> violations, String message) {
    super(message);
    this.violations = violations;
    this.ofType = ofType;
  }

  public ConstraintViolationException(
      Class<?> ofType, List<FieldError> violations, String message, Throwable cause) {
    super(message, cause);
    this.violations = violations;
    this.ofType = ofType;
  }

  public ConstraintViolationException(
      Class<?> ofType, List<FieldError> violations, Throwable cause) {
    super(cause);
    this.violations = violations;
    this.ofType = ofType;
  }

  protected ConstraintViolationException(
      Class<?> ofType,
      List<FieldError> violations,
      String message,
      Throwable cause,
      boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.violations = violations;
    this.ofType = ofType;
  }
}
