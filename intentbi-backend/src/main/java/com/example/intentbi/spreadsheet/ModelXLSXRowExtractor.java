package com.example.intentbi.spreadsheet;

import com.example.intentbi.exception.InvalidXLSXCellTypeException;
import com.example.intentbi.exception.InvalidXLSXHeaderException;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;

@FunctionalInterface
public interface ModelXLSXRowExtractor<T> {

  T extract(Map<String, Cell> cells)
      throws InvalidXLSXHeaderException, InvalidXLSXCellTypeException;
}
