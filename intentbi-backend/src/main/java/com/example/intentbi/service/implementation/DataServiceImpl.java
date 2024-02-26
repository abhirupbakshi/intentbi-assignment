package com.example.intentbi.service.implementation;

import com.example.intentbi.exception.DataAbsentException;
import com.example.intentbi.exception.InvalidPaginationException;
import com.example.intentbi.exception.InvalidSortByPropertyException;
import com.example.intentbi.model.Data;
import com.example.intentbi.repository.DataRepository;
import com.example.intentbi.service.DataService;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.reflect.Field;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class DataServiceImpl implements DataService {

  private static final Map<String, String> sortByProperties; // JSON to Field name map

  static {
    sortByProperties = new HashMap<>();

    for (Field field : Data.class.getDeclaredFields()) {
      JsonProperty property = field.getAnnotation(JsonProperty.class);
      String json = property == null ? field.getName() : property.value();

      sortByProperties.put(json, field.getName());
    }
  }

  private final DataRepository dataRepository;

  @Autowired
  public DataServiceImpl(DataRepository dataRepository) {
    this.dataRepository = dataRepository;
  }

  @Override
  public Page<Data> findAll(
      int pageNo, int pageSize, List<Map.Entry<String, Sort.Direction>> jsonSortByInfo) {
    List<String> invalidSortBy = new ArrayList<>();
    List<Sort.Order> orders = new ArrayList<>();

    if (pageNo < 1 || pageSize < 1) {
      throw new InvalidPaginationException();
    }

    for (Map.Entry<String, Sort.Direction> info : Objects.requireNonNull(jsonSortByInfo)) {
      String fName = sortByProperties.get(info.getKey());
      Sort.Direction direction = info.getValue();

      if (fName == null) {
        invalidSortBy.add(info.getKey());
      } else {
        orders.add(
            direction.equals(Sort.Direction.ASC) ? Sort.Order.asc(fName) : Sort.Order.desc(fName));
      }
    }
    if (!invalidSortBy.isEmpty()) {
      throw new InvalidSortByPropertyException(
          invalidSortBy, sortByProperties.keySet().stream().toList());
    }

    Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(orders));

    return dataRepository.findAll(pageable);
  }

  @Override
  public List<Data> saveAll(List<Data> dataList) {
    for (Data data : Objects.requireNonNull(dataList)) {
      Objects.requireNonNull(data).setId(null);
    }

    return dataRepository.saveAll(dataList);
  }

  @Override
  public Data update(long id, Data updatedData) {
    if (!dataRepository.existsById(id)) {
      throw new DataAbsentException(id);
    }

    return dataRepository.save(updatedData.setId(id));
  }

  @Override
  public Data delete(long id) {
    Data data = dataRepository.findById(id).orElseThrow(() -> new DataAbsentException(id));

    dataRepository.deleteById(id);
    return data;
  }
}
