package com.example.intentbi.spreadsheet.implementation.mapper;

import com.example.intentbi.spreadsheet.Mapper;
import java.time.Month;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.stereotype.Component;

@Component
public class MonthNameMapper extends AbstractMapper implements Mapper<Month> {

  @Override
  public boolean supports(Class<?> type) {
    return type.equals(Month.class);
  }

  private Month fromStr(String str) {
    for (Month month : Month.values()) {
      if (str.equalsIgnoreCase(month.name())) {
        return month;
      }
    }

    throw new IllegalArgumentException();
  }

  @Override
  public Month map(Cell cell, boolean nullable) {
    if (nullable && cell.getCellType().equals(CellType.BLANK)) {
      return null;
    }

    try {
      return fromStr(cell.getStringCellValue());
    } catch (Exception e) {
      throw createInvalidXLSXCellTypeException(cell, "month name", e);
    }
  }
}
