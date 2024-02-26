export enum DiscountBand {
  NONE = "NONE",
  LOW = "LOW",
  MEDIUM = "MEDIUM",
  HIGH = "HIGH"
}

export enum Month {
  JANUARY = "JANUARY",
  FEBRUARY = "FEBRUARY",
  MARCH = "MARCH",
  APRIL = "APRIL",
  MAY = "MAY",
  JUNE = "JUNE",
  JULY = "JULY",
  AUGUST = "AUGUST",
  SEPTEMBER = "SEPTEMBER",
  OCTOBER = "OCTOBER",
  NOVEMBER = "NOVEMBER",
  DECEMBER = "DECEMBER"
}

export class Data {
  id?: number;
  market?: string;
  country?: string;
  product?: string;
  discountBand?: DiscountBand;
  discount?: number;
  unitsSold?: number;
  manufacturingPrice?: number;
  salePrice?: number;
  grossSales?: number;
  sales?: number;
  COGS?: number;
  profit?: number;
  date?: Date;
  monthNumber?: number;
  monthName?: Month;
  year?: number;
}

export function discountBandFromStr(str: string) {
  return (DiscountBand as any)[str.toUpperCase()];
}

export function monthFromStr(str: string) {
  return (Month as any)[str.toUpperCase()];
}

export function dataFromJson(json: any) {
  let data = new Data();

  data.id = json.id;
  data.market = json.market;
  data.country = json.country;
  data.product = json.product;
  data.discountBand = discountBandFromStr(json.discount_band);
  data.discount = json.discount;
  data.unitsSold = json.units_sold;
  data.manufacturingPrice = json.manufacturing_price;
  data.salePrice = json.sale_price;
  data.grossSales = json.gross_sales;
  data.sales = json.sales;
  data.COGS = json.cogs;
  data.profit = json.profit;
  data.date = new Date(json.date); // Possible error condition
  data.monthNumber = json.month_number;
  data.monthName = monthFromStr(json.month_name);
  data.year = json.year;

  return data;
}

export function dataFieldToJsonField(field: string) {
  switch (field) {
    case "discountBand":
      return "discount_band";
    case "unitsSold":
      return "units_sold";
    case "manufacturingPrice":
      return "manufacturing_price";
    case "salePrice":
      return "sale_price";
    case "grossSales":
      return "gross_sales";
    case "monthNumber":
      return "month_number";
    case "monthName":
      return "month_name";
    case "COGS":
      return "cogs";
    default:
      return field;
  }
}
