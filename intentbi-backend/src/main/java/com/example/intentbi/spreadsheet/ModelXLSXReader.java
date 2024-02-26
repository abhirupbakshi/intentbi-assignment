package com.example.intentbi.spreadsheet;

import com.example.intentbi.exception.InvalidXLSXCellTypeException;
import com.example.intentbi.exception.InvalidXLSXHeaderException;
import com.example.intentbi.exception.InvalidXLSXHeaderFormatException;
import java.util.List;
import java.util.Map;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public interface ModelXLSXReader<T> {

  List<Map.Entry<Integer, T>> extractFromSheet(XSSFSheet sheet)
      throws InvalidXLSXHeaderFormatException,
          InvalidXLSXHeaderException,
          InvalidXLSXCellTypeException;

  List<Map.Entry<Integer, T>> extractFromWorkbook(XSSFWorkbook workbook, int... sheets)
      throws InvalidXLSXHeaderFormatException,
          InvalidXLSXHeaderException,
          InvalidXLSXCellTypeException;
}
