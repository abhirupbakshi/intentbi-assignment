package com.example.intentbi.spreadsheet.implementation.mapper;

import com.example.intentbi.spreadsheet.Mapper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.stereotype.Component;

@Component
public class LongMapper extends AbstractMapper implements Mapper<Long> {

  @Override
  public boolean supports(Class<?> type) {
    return type.equals(Long.class);
  }

  @Override
  public Long map(Cell cell, boolean nullable) {
    if (nullable && cell.getCellType().equals(CellType.BLANK)) {
      return null;
    }

    try {
      double value = cell.getNumericCellValue();

      if ((long) value != value) {
        throw new IllegalArgumentException();
      }

      return (long) value;
    } catch (Exception e) {
      throw createInvalidXLSXCellTypeException(cell, "whole number");
    }
  }
}
