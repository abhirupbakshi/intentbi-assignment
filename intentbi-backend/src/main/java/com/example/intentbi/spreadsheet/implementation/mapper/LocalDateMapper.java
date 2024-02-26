package com.example.intentbi.spreadsheet.implementation.mapper;

import com.example.intentbi.spreadsheet.Mapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.stereotype.Component;

@Component
public class LocalDateMapper extends AbstractMapper implements Mapper<LocalDate> {

  @Override
  public boolean supports(Class<?> type) {
    return type.equals(LocalDate.class);
  }

  @Override
  public LocalDate map(Cell cell, boolean nullable) {
    if (nullable && cell.getCellType().equals(CellType.BLANK)) {
      return null;
    }

    try {
      Date date = cell.getDateCellValue();
      return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    } catch (NumberFormatException e) {
      throw createInvalidXLSXCellTypeException(cell, "date");
    }
  }
}
