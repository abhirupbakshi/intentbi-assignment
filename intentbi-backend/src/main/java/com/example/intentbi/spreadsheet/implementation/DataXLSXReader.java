package com.example.intentbi.spreadsheet.implementation;

import com.example.intentbi.exception.InvalidXLSXCellTypeException;
import com.example.intentbi.exception.InvalidXLSXHeaderException;
import com.example.intentbi.exception.InvalidXLSXHeaderFormatException;
import com.example.intentbi.model.Data;
import com.example.intentbi.spreadsheet.ModelXLSXReader;
import com.example.intentbi.spreadsheet.ModelXLSXRowExtractor;
import com.example.intentbi.spreadsheet.XLSXProperty;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataXLSXReader implements ModelXLSXReader<Data> {

  private static final Set<String> headers;

  static {
    headers =
        Arrays.stream(Data.class.getDeclaredFields())
            .map(
                f -> {
                  XLSXProperty property = f.getAnnotation(XLSXProperty.class);
                  return property == null ? f.getName() : property.value();
                })
            .collect(Collectors.toSet());
  }

  private final ModelXLSXRowExtractor<Data> extractor;

  @Autowired
  public DataXLSXReader(ModelXLSXRowExtractor<Data> extractor) {
    this.extractor = extractor;
  }

  private boolean containsAlternateName(String name) {
    for (Field field : Data.class.getDeclaredFields()) {
      XLSXProperty property = field.getAnnotation(XLSXProperty.class);

      if (property == null) {
        continue;
      }

      if (Arrays.asList(property.alternatives()).contains(name)) {
        return true;
      }
    }

    return false;
  }

  private String[] extractHeaders(Row row) {
    List<String> _headers = new ArrayList<>();
    List<String> invalid = new ArrayList<>();

    for (int i = 0; true; i++) {
      Cell cell = row.getCell(i);

      if (cell == null || cell.getCellType().equals(CellType.BLANK)) {
        break;
      }

      try {
        String header = cell.getStringCellValue();
        if (!headers.contains(header) && !containsAlternateName(header)) {
          invalid.add(header);
        } else {
          _headers.add(header);
        }
      } catch (Exception e) {
        throw new InvalidXLSXHeaderFormatException(
            "Header cell with index " + i + " is does not have a string value", e);
      }
    }

    if (!invalid.isEmpty()) {
      throw new InvalidXLSXHeaderException(invalid, headers.stream().toList());
    }

    return _headers.toArray(new String[0]);
  }

  private Map<String, Cell> extractCellsWithHeaders(String[] headers, Row row) {
    Map<String, Cell> map = new HashMap<>();

    for (int i = 0; i < headers.length; i++) {
      map.put(headers[i], row.getCell(i));
    }

    return map;
  }

  @Override
  public List<Map.Entry<Integer, Data>> extractFromSheet(XSSFSheet sheet) {
    List<Map.Entry<Integer, Data>> dataList = new ArrayList<>();
    List<InvalidXLSXCellTypeException.CellInfo> invalidCoordinates = new ArrayList<>();
    String[] headers = null;

    for (Row row : sheet) {
      if (headers == null) {
        headers = extractHeaders(row);
        continue;
      }

      Map<String, Cell> cells = extractCellsWithHeaders(headers, row);
      Data data = null;
      try {
        data = extractor.extract(cells);
        dataList.add(Map.entry(row.getRowNum(), data));
      } catch (InvalidXLSXCellTypeException e) {
        invalidCoordinates.addAll(e.getCoordinates());
      }
    }

    if (!invalidCoordinates.isEmpty()) {
      throw new InvalidXLSXCellTypeException(invalidCoordinates);
    }

    return dataList;
  }

  @Override
  public List<Map.Entry<Integer, Data>> extractFromWorkbook(XSSFWorkbook workbook, int... sheets) {
    List<Map.Entry<Integer, Data>> dataList = new ArrayList<>();
    List<InvalidXLSXCellTypeException.CellInfo> invalidCoordinates = new ArrayList<>();

    if (sheets == null || sheets.length == 0) {
      sheets = new int[workbook.getNumberOfSheets()];
      for (int i = 0; i < sheets.length; i++) sheets[i] = i;
    }

    for (int no : sheets) {
      XSSFSheet sheet = workbook.getSheetAt(no);

      try {
        List<Map.Entry<Integer, Data>> list = extractFromSheet(sheet);
        dataList.addAll(list);
      } catch (InvalidXLSXCellTypeException e) {
        invalidCoordinates.addAll(e.getCoordinates());
      }
    }

    if (!invalidCoordinates.isEmpty()) {
      throw new InvalidXLSXCellTypeException(invalidCoordinates);
    }

    return dataList;
  }
}
