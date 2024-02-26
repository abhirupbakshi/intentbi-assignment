package com.example.intentbi.spreadsheet;

import com.example.intentbi.exception.InvalidXLSXCellTypeException;
import org.apache.poi.ss.usermodel.Cell;

public interface Mapper<T> {

  boolean supports(Class<?> type);

  T map(Cell cell, boolean nullable) throws InvalidXLSXCellTypeException;
}
