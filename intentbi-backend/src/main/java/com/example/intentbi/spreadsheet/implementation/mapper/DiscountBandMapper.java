package com.example.intentbi.spreadsheet.implementation.mapper;

import com.example.intentbi.model.DiscountBand;
import com.example.intentbi.spreadsheet.Mapper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.stereotype.Component;

@Component
public class DiscountBandMapper extends AbstractMapper implements Mapper<DiscountBand> {

  @Override
  public boolean supports(Class<?> type) {
    return type.equals(DiscountBand.class);
  }

  @Override
  public DiscountBand map(Cell cell, boolean nullable) {
    if (nullable && cell.getCellType().equals(CellType.BLANK)) {
      return null;
    }

    try {
      return DiscountBand.fromStr(cell.getStringCellValue());
    } catch (Exception e) {
      throw createInvalidXLSXCellTypeException(cell, "discount band", e);
    }
  }
}
