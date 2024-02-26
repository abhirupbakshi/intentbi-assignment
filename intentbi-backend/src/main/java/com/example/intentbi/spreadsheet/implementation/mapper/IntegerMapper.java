package com.example.intentbi.spreadsheet.implementation.mapper;

import com.example.intentbi.spreadsheet.Mapper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.stereotype.Component;

@Component
public class IntegerMapper extends AbstractMapper implements Mapper<Integer> {

  @Override
  public boolean supports(Class<?> type) {
    return type.equals(Integer.class);
  }

  @Override
  public Integer map(Cell cell, boolean nullable) {
    if (nullable && cell.getCellType().equals(CellType.BLANK)) {
      return null;
    }

    try {
      double value = cell.getNumericCellValue();

      if ((int) value != value) {
        throw new IllegalArgumentException();
      }

      return (int) value;
    } catch (Exception e) {
      throw createInvalidXLSXCellTypeException(cell, "whole number");
    }
  }
}
