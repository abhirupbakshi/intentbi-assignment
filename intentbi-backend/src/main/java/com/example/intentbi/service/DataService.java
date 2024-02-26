package com.example.intentbi.service;

import com.example.intentbi.exception.DataAbsentException;
import com.example.intentbi.exception.InvalidPaginationException;
import com.example.intentbi.exception.InvalidSortByPropertyException;
import com.example.intentbi.model.Data;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface DataService {

  Page<Data> findAll(
      int pageNo, int pageSize, List<Map.Entry<String, Sort.Direction>> jsonSortByInfo)
      throws NullPointerException, InvalidPaginationException, InvalidSortByPropertyException;

  List<Data> saveAll(List<Data> dataList) throws NullPointerException;

  // Have to provide all field information everytime
  Data update(long id, Data updatedData) throws DataAbsentException;

  Data delete(long id) throws DataAbsentException;
}
