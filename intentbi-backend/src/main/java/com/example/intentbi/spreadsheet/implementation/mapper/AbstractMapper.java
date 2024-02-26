package com.example.intentbi.spreadsheet.implementation.mapper;

import com.example.intentbi.exception.InvalidXLSXCellTypeException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;

public class AbstractMapper {

  protected InvalidXLSXCellTypeException createInvalidXLSXCellTypeException(
      Cell cell, String expected, Throwable e) {
    int row = cell.getRow().getRowNum();
    int col = cell.getColumnIndex();
    InvalidXLSXCellTypeException.CellInfo info =
        new InvalidXLSXCellTypeException.CellInfo(row, col, expected);
    return new InvalidXLSXCellTypeException(new ArrayList<>(List.of(info)), e);
  }

  protected InvalidXLSXCellTypeException createInvalidXLSXCellTypeException(
      Cell cell, String expected) {
    return createInvalidXLSXCellTypeException(cell, expected, null);
  }
}
