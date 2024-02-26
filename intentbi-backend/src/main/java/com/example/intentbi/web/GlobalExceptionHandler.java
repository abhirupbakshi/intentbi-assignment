package com.example.intentbi.web;

import com.example.intentbi.exception.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolation;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import org.apache.poi.EmptyFileException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(InvalidXLSXHeaderFormatException.class)
  public ProblemDetail handleInvalidXLSXHeaderFormatException(InvalidXLSXHeaderFormatException e) {
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    pd.setType(URI.create("invalid-xlsx-header-format"));
    pd.setTitle("Given XLSX file contains malformed header row");
    pd.setDetail("");

    return pd;
  }

  @ExceptionHandler(InvalidXLSXHeaderException.class)
  public ProblemDetail handleInvalidXLSXHeaderFormatException(InvalidXLSXHeaderException e) {
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    pd.setType(URI.create("invalid-xlsx-header"));
    pd.setTitle("Given XLSX file contains malformed header values");
    pd.setProperty("invalid_headers", e.getInvalidHeaders());
    pd.setProperty("acceptable_herders", e.getValidHeaders());

    return pd;
  }

  @ExceptionHandler(InvalidXLSXCellTypeException.class)
  public ProblemDetail handleInvalidXLSXHeaderFormatException(InvalidXLSXCellTypeException e) {
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    pd.setType(URI.create("invalid-xlsx-cell-type"));
    pd.setTitle("Given XLSX file contains cell values of invalid type");
    pd.setProperty("invalid_cells", e.getCoordinates());

    return pd;
  }

  private String toJsonProperty(Field field) {
    JsonProperty property = field.getAnnotation(JsonProperty.class);
    return property == null ? field.getName() : property.value();
  }

  @SneakyThrows
  private String toJsonProperty(Class<?> type, String name) {
    return toJsonProperty(type.getDeclaredField(name));
  }

  @ExceptionHandler(XLSXConstraintViolationException.class)
  @SneakyThrows
  public ProblemDetail handleXLSXConstraintViolationException(XLSXConstraintViolationException e) {
    List<Map<String, Object>> messages = new ArrayList<>();
    Map<Integer, List<ConstraintViolation<Object>>> violations = e.getViolations();
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    pd.setType(URI.create("xlsx-constraint-violation"));
    pd.setTitle("Given XLSX file contains values of that violates data constraints");
    pd.setProperty("constraint_violations", messages);

    for (Map.Entry<Integer, List<ConstraintViolation<Object>>> entry : violations.entrySet()) {
      Map<String, String> ms = new HashMap<>();
      messages.add(Map.of("row_no", entry.getKey(), "messages", ms));

      for (ConstraintViolation<Object> violation : entry.getValue()) {
        Field field = e.getOfType().getDeclaredField(violation.getPropertyPath().toString());
        ms.put(toJsonProperty(field), violation.getMessage());
      }
    }

    return pd;
  }

  @ExceptionHandler(DataAbsentException.class)
  public ProblemDetail handleDataAbsentException(DataAbsentException e) {
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
    pd.setType(URI.create("data-absent"));
    pd.setTitle("Data with id " + e.getId() + " does not exist");

    return pd;
  }

  @ExceptionHandler(UserExistsException.class)
  public ProblemDetail handleUserExistsException(UserExistsException e) {
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
    pd.setType(URI.create("user-exists"));
    pd.setTitle("User with username " + e.getUsername() + " already exist");

    return pd;
  }

  @ExceptionHandler(UserAbsentException.class)
  public ProblemDetail handleUserAbsentException(UserAbsentException e) {
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
    pd.setType(URI.create("user-absent"));
    pd.setTitle("User with username " + e.getUsername() + " does not exist");

    return pd;
  }

  @ExceptionHandler(InvalidXLSXFileTypeException.class)
  public ProblemDetail handleInvalidXLSXFileType(InvalidXLSXFileTypeException e) {
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    pd.setType(URI.create("invalid-xlsx-type"));
    pd.setTitle("Given file is not a valid XLSX file");

    return pd;
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ProblemDetail handleConstraintViolationException(ConstraintViolationException e) {
    List<FieldError> violations = e.getViolations();
    Map<String, String> messages = new HashMap<>();
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    pd.setType(URI.create("constraint-violation"));
    pd.setTitle("Some values violates data constraints");
    pd.setProperty("constraint_violations", messages);

    for (FieldError violation : violations) {
      messages.put(
          toJsonProperty(e.getOfType(), violation.getField()), violation.getDefaultMessage());
    }

    return pd;
  }

  @ExceptionHandler(InvalidPaginationException.class)
  public ProblemDetail handleInvalidPaginationException(InvalidPaginationException e) {
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    pd.setType(URI.create("invalid-pagination"));
    pd.setTitle("Given pagination information is incorrect");
    pd.setDetail("Both page number and page size should be greater or equal to 1");

    return pd;
  }

  @ExceptionHandler(InvalidSortByPropertyException.class)
  public ProblemDetail handleInvalidSortByPropertyException(InvalidSortByPropertyException e) {
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    pd.setType(URI.create("invalid-sort-by-property"));
    pd.setTitle("Given sort by property is invalid");
    pd.setProperty("invalid_properties", e.getInvalidSortInfo());
    pd.setProperty("valid_properties", e.getValidProperties());

    return pd;
  }

  @ExceptionHandler(InvalidSortDirectionException.class)
  public ProblemDetail handleInvalidSortDirectionException(InvalidSortDirectionException e) {
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    pd.setType(URI.create("invalid-sort-direction"));
    pd.setTitle("Given sort direction is invalid");
    pd.setDetail("Sort direction should be 'ASC' or 'DESC' only (both case insensitive)");
    pd.setProperty("properties", e.getForProperties());

    return pd;
  }

  @ExceptionHandler(EmptyFileException.class)
  public ProblemDetail handleEmptyFileException(EmptyFileException e) {
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    pd.setType(URI.create("empty-file"));
    pd.setTitle("Given file contains no data");

    return pd;
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    String errorMessage = ex.getMessage();
    InvalidFormatException cause = (InvalidFormatException) ex.getCause();

    if (ex.getCause() instanceof  InvalidFormatException ife) {
      List<String> fields = new ArrayList<>();

      for (JsonMappingException.Reference reference : ife.getPath()) {
        fields.add(reference.getFieldName());
      }

      ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
      pd.setType(URI.create("invalid-property-type"));
      pd.setTitle("Given request contains properties that have invalid value type");
      pd.setProperty("invalid_properties", fields);

      return ResponseEntity.badRequest().body(pd);
    } else {
      return super.handleHttpMessageNotReadable(ex, headers, status, request);
    }
  }
}
