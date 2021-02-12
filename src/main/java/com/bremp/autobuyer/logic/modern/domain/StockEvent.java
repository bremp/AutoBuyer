package com.bremp.autobuyer.logic.modern.domain;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class StockEvent {

  private final StockEventType type;
  private final Map<String, String> fields;

  public StockEvent(StockEventType type, Map<String, String> fields) {
    this.type = type;
    this.fields = fields;
  }

  public StockEventType getType() {
    return type;
  }

  public int getCurrentPrice() {
    return getInt("CurrentPrice");
  }

  public int getNumberInStock() {
    return getInt("NumberInStock");
  }

  public int getNumberSold() {
    return getInt("NumberSold");
  }

  public String getBuyerName() {
    return get("BuyerName");
  }

  private int getInt(String fieldName) {
    return Integer.parseInt(get(fieldName));
  }

  private String get(String fieldName) {
    String value = fields.get(fieldName);
    if (value == null) {
      throw new UnsupportedOperationException(fieldName);
    }
    return value;
  }

  public static StockEvent from(String message) {
    if (!message.contains(":") || !message.contains(";")) {
      throw new IllegalArgumentException();
    }

    Map<String, String> fields = getFields(message);
    StockEventType eventType = getEventType(fields);

    return new StockEvent(eventType, fields);
  }

  private static StockEventType getEventType(Map<String, String> fields) {
    if (!fields.containsKey("Event")) {
      throw new IllegalArgumentException();
    }

    StockEventType type = StockEventType
        .valueOf(fields.get("Event").toUpperCase());
    if (type == null) {
      throw new IllegalArgumentException();
    }

    return type;
  }

  private static Map<String, String> getFields(String message) {
    Map<String, String> fields = new LinkedHashMap<>();
    String[] pairs = message.split(";");
    for (String pair : pairs) {
      String[] data = pair.split(":");
      fields.put(data[0].trim(), data[1].trim());
    }

    return fields;
  }

  public static StockEvent close() {
    return from("Event: CLOSE;");
  }

  public static StockEvent price(int currentPrice, int numberInStock) {
    return from(String.format("Event: PRICE; CurrentPrice: %d; NumberInStock: %d", currentPrice, numberInStock));
  }

  public static StockEvent purchase(String buyerName, int numberSold)
  {
    return from(String.format("Event: PURCHASE; BuyerName: %s; NumberSold: %d", buyerName, numberSold));
  }

  @Override
  public String toString() {
    return fields.entrySet().stream()
        .map(e -> e.getKey() + ": " + e.getValue() + ";")
        .collect(Collectors.joining(" "));
  }

}
