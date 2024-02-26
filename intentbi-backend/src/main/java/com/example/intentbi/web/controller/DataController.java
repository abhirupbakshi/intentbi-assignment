package com.example.intentbi.web.controller;

import com.example.intentbi.configuration.Constants;
import com.example.intentbi.exception.ConstraintViolationException;
import com.example.intentbi.exception.InvalidSortDirectionException;
import com.example.intentbi.exception.InvalidXLSXFileTypeException;
import com.example.intentbi.exception.XLSXConstraintViolationException;
import com.example.intentbi.model.Data;
import com.example.intentbi.service.DataService;
import com.example.intentbi.spreadsheet.ModelXLSXReader;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(Constants.API_URI_PREFIX + "/data")
public class DataController {

  private final ModelXLSXReader<Data> xlsxReader;
  private final DataService dataService;
  private final Validator validator;

  @Autowired
  public DataController(
      ModelXLSXReader<Data> xlsxReader, DataService dataService, Validator validator) {
    this.xlsxReader = xlsxReader;
    this.dataService = dataService;
    this.validator = validator;
  }

  private List<Map.Entry<String, Sort.Direction>> createSortingInfo(List<String> sort) {
    List<Map.Entry<String, Sort.Direction>> sortBy = new ArrayList<>();
    List<String> invalidDirectionProperties = new ArrayList<>();

    if (sort == null) {
      return sortBy;
    }

    for (String s : sort) {
      String[] split = s.split(":");
      Sort.Direction d = null;

      if (split.length > 1 && split[1].equalsIgnoreCase("asc")) {
        d = Sort.Direction.ASC;
      } else if (split.length > 1 && split[1].equalsIgnoreCase("desc")) {
        d = Sort.Direction.DESC;
      } else {
        invalidDirectionProperties.add(split[0]);
      }

      if (d != null) {
        sortBy.add(Map.entry(split[0], d));
      }
    }

    if (!invalidDirectionProperties.isEmpty()) {
      throw new InvalidSortDirectionException(invalidDirectionProperties);
    }

    return sortBy;
  }

  @GetMapping
  public ResponseEntity<Map<String, Object>> findAll(
      @RequestParam(value = "_page", required = false, defaultValue = "1") int pageNo,
      @RequestParam(value = "_size", required = false, defaultValue = "10") int pageSize,
      @RequestParam(value = "_sort", required = false) List<String> sort) {
    List<Map.Entry<String, Sort.Direction>> sortBy = createSortingInfo(sort);
    Page<Data> page = dataService.findAll(pageNo, pageSize, sortBy);
    Map<String, Object> response = new HashMap<>();
    HttpHeaders headers = new HttpHeaders();

    response.put("data", page.getContent());
    response.put("X-Total-Count", page.getTotalElements());
    headers.add("X-Total-Count", String.valueOf(page.getTotalElements()));
    headers.add("Access-Control-Expose-Headers", "X-Total-Count");

    return ResponseEntity.ok().headers(headers).body(response);
  }

  @PostMapping
  public ResponseEntity<Data> save(
      @Valid @RequestBody Data updatedData, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      throw new ConstraintViolationException(Data.class, bindingResult.getFieldErrors());
    }

    Data updated = dataService.saveAll(List.of(updatedData)).get(0);

    return ResponseEntity.ok(updated);
  }

  @PostMapping("/xlsx")
  public ResponseEntity<List<Data>> saveWithXLSX(
      @RequestParam(value = "_sheet", required = false) int[] sheets,
      @RequestParam("_file") MultipartFile file,
      HttpServletRequest request)
      throws IOException {

    if (!new Tika().detect(file.getInputStream()).equals("application/zip")) {
      throw new InvalidXLSXFileTypeException();
    }

    XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
    List<Map.Entry<Integer, Data>> dataList = xlsxReader.extractFromWorkbook(workbook, sheets);
    Map<Integer, List<ConstraintViolation<Object>>> violations = new HashMap<>();

    for (Map.Entry<Integer, Data> entry : dataList) {
      Set<ConstraintViolation<Object>> cvs = validator.validate(entry.getValue());

      if (!cvs.isEmpty()) {
        violations.put(entry.getKey(), cvs.stream().toList());
      }
    }

    if (!violations.isEmpty()) {
      throw new XLSXConstraintViolationException(Data.class, violations);
    }

    List<Data> saved = dataService.saveAll(dataList.stream().map(Map.Entry::getValue).toList());

    return ResponseEntity.created(URI.create(request.getRequestURI())).body(saved);
  }

  @PostMapping("/{id}")
  public ResponseEntity<Data> update(
      @PathVariable("id") long id,
      @Valid @RequestBody Data updatedData,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      throw new ConstraintViolationException(Data.class, bindingResult.getFieldErrors());
    }

    Data updated = dataService.update(id, updatedData);

    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Data> delete(@PathVariable("id") long id) {
    Data deleted = dataService.delete(id);

    return ResponseEntity.ok(deleted);
  }
}
