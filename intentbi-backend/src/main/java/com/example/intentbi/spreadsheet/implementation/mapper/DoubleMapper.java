package com.example.intentbi.spreadsheet.implementation.mapper;

import com.example.intentbi.spreadsheet.Mapper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.stereotype.Component;

@Component
public class DoubleMapper extends AbstractMapper implements Mapper<Double> {

  @Override
  public boolean supports(Class<?> type) {
    return type.equals(Double.class);
  }

  @Override
  public Double map(Cell cell, boolean nullable) {
    if (nullable && cell.getCellType().equals(CellType.BLANK)) {
      return null;
    }

    try {
      return cell.getNumericCellValue();
    } catch (Exception e) {
      throw createInvalidXLSXCellTypeException(cell, "fractional number");
    }
  }
}
