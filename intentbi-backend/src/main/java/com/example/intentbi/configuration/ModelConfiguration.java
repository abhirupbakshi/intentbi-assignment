package com.example.intentbi.configuration;

import com.example.intentbi.model.Data;
import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.Map;
import java.util.function.BiFunction;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelConfiguration {

  @Bean
  @SneakyThrows
  public BiFunction<Field, Cell, Map.Entry<Object, Boolean>> dataAdditionalValuesHandler() {
    Field profit = Data.class.getDeclaredField("profit");
    Map.Entry<Object, Boolean> unHandled = new AbstractMap.SimpleEntry<>(null, false);

    return (field, cell) -> {
      if (field.equals(profit)) {
        try {
          String value = cell.getStringCellValue();
          return value.equals("$-") ? new AbstractMap.SimpleEntry<>(null, true) : unHandled;
        } catch (Exception ignored) {
        }

        return unHandled;
      }

      return unHandled;
    };
  }
}
