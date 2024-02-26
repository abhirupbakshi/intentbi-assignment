package com.example.intentbi.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidXLSXCellTypeException extends RuntimeException {

  private final List<CellInfo> coordinates;

  public InvalidXLSXCellTypeException(List<CellInfo> coordinates) {
    super();
    this.coordinates = coordinates;
  }

  public InvalidXLSXCellTypeException(List<CellInfo> coordinates, String message) {
    super(message);
    this.coordinates = coordinates;
  }

  public InvalidXLSXCellTypeException(List<CellInfo> coordinates, String message, Throwable cause) {
    super(message, cause);
    this.coordinates = coordinates;
  }

  public InvalidXLSXCellTypeException(List<CellInfo> coordinates, Throwable cause) {
    super(cause);
    this.coordinates = coordinates;
  }

  protected InvalidXLSXCellTypeException(
      List<CellInfo> coordinates,
      String message,
      Throwable cause,
      boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.coordinates = coordinates;
  }

  public record CellInfo(
      @JsonProperty("row_index") int row,
      @JsonProperty("col_index") int col,
      @JsonProperty("expected_type") String expected) {}
}
