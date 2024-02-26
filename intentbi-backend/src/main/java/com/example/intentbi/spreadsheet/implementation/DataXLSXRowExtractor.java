package com.example.intentbi.spreadsheet.implementation;

import com.example.intentbi.exception.InvalidXLSXCellTypeException;
import com.example.intentbi.exception.InvalidXLSXHeaderException;
import com.example.intentbi.model.Data;
import com.example.intentbi.spreadsheet.Mapper;
import com.example.intentbi.spreadsheet.ModelXLSXRowExtractor;
import com.example.intentbi.spreadsheet.XLSXProperty;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiFunction;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class DataXLSXRowExtractor implements ModelXLSXRowExtractor<Data> {

  private static final Map<String, Field> fields;

  static {
    Field[] fs = Data.class.getDeclaredFields();
    fields = new HashMap<>();

    for (Field f : fs) {
      XLSXProperty property = f.getAnnotation(XLSXProperty.class);
      fields.put(property == null ? f.getName() : property.value(), f);
    }
  }

  private final BiFunction<Field, Cell, Map.Entry<Object, Boolean>> dataAdditionalValuesHandler;
  private final Mapper<?>[] mappers;

  @Autowired
  public DataXLSXRowExtractor(
      @Qualifier("dataAdditionalValuesHandler")
          BiFunction<Field, Cell, Map.Entry<Object, Boolean>> dataAdditionalValuesHandler,
      ListableBeanFactory beanFactory) {
    this.dataAdditionalValuesHandler = dataAdditionalValuesHandler;
    this.mappers = beanFactory.getBeansOfType(Mapper.class).values().toArray(new Mapper[0]);
  }

  @SneakyThrows
  private void setValue(Object object, Field field, Object value) {
    String name = field.getName();
    String pCase = Character.toUpperCase(name.charAt(0)) + name.substring(1);
    Method setter = Data.class.getMethod("set" + pCase, field.getType());

    setter.invoke(object, value);
  }

  private boolean isNullable(Field field) {
    XLSXProperty property = field.getAnnotation(XLSXProperty.class);
    return property == null || property.nullable();
  }

  private Field fromAlternateName(String name) {
    for (Field field : Data.class.getDeclaredFields()) {
      XLSXProperty property = field.getAnnotation(XLSXProperty.class);

      if (property == null) {
        continue;
      }

      if (Arrays.asList(property.alternatives()).contains(name)) {
        return field;
      }
    }

    return null;
  }

  @Override
  public Data extract(Map<String, Cell> cells) {
    Data data = new Data();
    List<InvalidXLSXCellTypeException.CellInfo> invalidCoordinates = new ArrayList<>();

    for (Map.Entry<String, Cell> entry : cells.entrySet()) {
      Field field = fromAlternateName(entry.getKey());

      if (!fields.containsKey(entry.getKey()) && field == null) {
        throw new InvalidXLSXHeaderException(
            new ArrayList<>(List.of(entry.getKey())), fields.keySet().stream().toList());
      }

      field = field == null ? fields.get(entry.getKey()) : field;
      Cell cell = entry.getValue();
      boolean nullable = isNullable(field);
      Mapper<?> mapper = null;

      for (int i = 0; mapper == null && i < mappers.length; i++) {
        mapper = mappers[i].supports(field.getType()) ? mappers[i] : null;
      }

      if (mapper == null) {
        throw new RuntimeException("No mapper found for type " + field.getType());
      }

      try {
        Map.Entry<Object, Boolean> result = dataAdditionalValuesHandler.apply(field, cell);
        setValue(data, field, result.getValue() ? result.getKey() : mapper.map(cell, nullable));
      } catch (InvalidXLSXCellTypeException e) {
        invalidCoordinates.addAll(e.getCoordinates());
      }
    }

    if (!invalidCoordinates.isEmpty()) {
      throw new InvalidXLSXCellTypeException(invalidCoordinates);
    }

    return data;
  }
}
