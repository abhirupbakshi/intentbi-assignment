package com.example.intentbi.spreadsheet;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface XLSXProperty {

  String value() default "";

  String[] alternatives() default {};

  boolean nullable() default true;
}
