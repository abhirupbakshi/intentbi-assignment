package com.example.intentbi.spreadsheet.implementation.mapper;

import com.example.intentbi.spreadsheet.Mapper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.stereotype.Component;

@Component
public class StringMapper extends AbstractMapper implements Mapper<String> {

  @Override
  public boolean supports(Class<?> type) {
    return type.equals(String.class);
  }

  @Override
  public String map(Cell cell, boolean nullable) {
    if (nullable && cell.getCellType().equals(CellType.BLANK)) {
      return null;
    }

    try {
      return cell.getStringCellValue();
    } catch (Exception e) {
      throw createInvalidXLSXCellTypeException(cell, "string", e);
    }
  }
}
