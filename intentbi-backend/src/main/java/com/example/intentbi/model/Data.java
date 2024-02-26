package com.example.intentbi.model;

import com.example.intentbi.spreadsheet.XLSXProperty;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor(force = true)
@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "data")
public class Data {

  @JsonProperty("id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @JsonProperty("market")
  @XLSXProperty(value = "Market", alternatives = "Segment")
  @NotBlank(message = "market property is empty")
  @Column(name = "market")
  private String market;

  @JsonProperty("country")
  @XLSXProperty("Country")
  @NotBlank(message = "country property is empty")
  @Column(name = "country")
  private String country;

  @JsonProperty("product")
  @XLSXProperty("Product")
  @NotBlank(message = "product property is empty")
  @Column(name = "product")
  private String product;

  @JsonProperty("discount_band")
  @XLSXProperty("Discount Band")
  @NotNull(message = "discount_band property is empty")
  @Column(name = "discount_band")
  private DiscountBand discountBand;

  @JsonProperty("discount")
  @XLSXProperty("Discounts")
  @Column(name = "discount")
  private BigDecimal discount;

  @JsonProperty("units_sold")
  @XLSXProperty("Units Sold")
  @NotNull(message = "units_sold property is empty")
  @Column(name = "units_sold")
  private Double unitsSold;

  @JsonProperty("manufacturing_price")
  @XLSXProperty("Manufacturing Price")
  @NotNull(message = "manufacturing_price property is empty")
  @Column(name = "manufacturing_price")
  private BigDecimal manufacturingPrice;

  @JsonProperty("sale_price")
  @XLSXProperty("Sale Price")
  @NotNull(message = "sale_price property is empty")
  @Column(name = "sale_price")
  private BigDecimal salePrice;

  @JsonProperty("gross_sales")
  @XLSXProperty("Gross Sales")
  @NotNull(message = "gross_sales property is empty")
  @Column(name = "gross_sales")
  private BigDecimal grossSales;

  @JsonProperty("sales")
  @XLSXProperty("Sales")
  @NotNull(message = "sales property is empty")
  @Column(name = "sales")
  private BigDecimal sales;

  @JsonProperty("cogs")
  @XLSXProperty("COGS")
  @NotNull(message = "cogs property is empty")
  @Column(name = "cogs")
  private BigDecimal COGS;

  @JsonProperty("profit")
  @XLSXProperty("Profit")
  @Column(name = "profit")
  private BigDecimal profit;

  @JsonProperty("date")
  @XLSXProperty("Date")
  @NotNull(message = "date property is empty")
  @Column(name = "date")
  private LocalDate date;

  @JsonProperty("month_number")
  @XLSXProperty("Month Number")
  @NotNull(message = "month_number property is empty")
  @Min(value = 1, message = "month_number minimum value can be 1")
  @Max(value = 12, message = "month_number maximum value can be 12")
  @Column(name = "month_number")
  private Integer monthNumber;

  @JsonProperty("month_name")
  @XLSXProperty("Month Name")
  @NotNull(message = "month_name property is empty")
  @Column(name = "month_name")
  private Month monthName;

  @JsonProperty("year")
  @XLSXProperty("Year")
  @NotNull(message = "year property is empty")
  @Column(name = "year")
  private Integer year;
}
