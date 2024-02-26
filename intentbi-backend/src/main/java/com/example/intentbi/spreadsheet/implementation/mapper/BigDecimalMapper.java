package com.example.intentbi.spreadsheet.implementation.mapper;

import com.example.intentbi.spreadsheet.Mapper;
import java.math.BigDecimal;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.stereotype.Component;

@Component
public class BigDecimalMapper extends AbstractMapper implements Mapper<BigDecimal> {

  @Override
  public boolean supports(Class<?> type) {
    return type.equals(BigDecimal.class);
  }

  @Override
  public BigDecimal map(Cell cell, boolean nullable) {
    if (nullable && cell.getCellType().equals(CellType.BLANK)) {
      return null;
    } else if (nullable && cell.getCellType().equals(CellType.STRING)) {
      return null;
    }

    try {
      return BigDecimal.valueOf(cell.getNumericCellValue());
    } catch (Exception e) {
      throw createInvalidXLSXCellTypeException(cell, "fractional number");
    }
  }
}
