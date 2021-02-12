package com.bremp.autobuyer.logic.modern.domain;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class StockEventTest {

  @Test
  void parses_close_event() {
    String message = "Event: CLOSE;";

    StockEvent stockEvent = StockEvent.from(message);
    String serialized = stockEvent.toString();

    assertThat(stockEvent.getType(), is(StockEventType.CLOSE));
    assertThat(serialized, is(message));
  }

  @Test
  void parses_price_event() {
    String message = "Event: PRICE; NumberInStock: 12; CurrentPrice: 34;";

    StockEvent stockEvent = StockEvent.from(message);
    String serialized = stockEvent.toString();

    assertThat(stockEvent.getType(), is(StockEventType.PRICE));
    assertThat(stockEvent.getNumberInStock(), is(12));
    assertThat(stockEvent.getCurrentPrice(), is(34));
    assertThat(serialized, is(message));
  }

  @Test
  void parses_purchase_event() {
    String message = "Event: PURCHASE; BuyerName: Buyer; NumberSold: 1;";

    StockEvent stockEvent = StockEvent.from(message);
    String serialized = stockEvent.toString();

    assertThat(stockEvent.getType(), is(StockEventType.PURCHASE));
    assertThat(stockEvent.getBuyerName(), is("Buyer"));
    assertThat(stockEvent.getNumberSold(), is(1));
    assertThat(serialized, is(message));
  }

  @Test
  void does_not_parse_events_with_incorrect_format() {
    String message = "incorrect message";

    assertThrows(IllegalArgumentException.class, () -> {
      StockEvent.from(message);
    });
  }

  @Test
  void does_not_parse_events_with_unknown_types() {
    String message = "Event: UNKNOWN;";

    assertThrows(IllegalArgumentException.class, () -> {
      StockEvent.from(message);
    });
  }

  @Test
  void close_method_returns_a_close_event() {
    StockEvent stockEvent = StockEvent.close();

    assertThat(stockEvent.getType(), is(StockEventType.CLOSE));
    assertThat(stockEvent.toString(), is("Event: CLOSE;"));
  }

  @Test
  void price_method_returns_a_price_event() {
    StockEvent stockEvent = StockEvent.price(10, 15);

    assertThat(stockEvent.getType(), is(StockEventType.PRICE));
    assertThat(stockEvent.getCurrentPrice(), is(10));
    assertThat(stockEvent.getNumberInStock(), is(15));
    assertThat(stockEvent.toString(), is("Event: PRICE; CurrentPrice: 10; NumberInStock: 15;"));
  }

  @Test
  void purchase_method_returns_a_purchase_event() {
    StockEvent stockEvent = StockEvent.purchase("some user", 1);

    assertThat(stockEvent.getType(), is(StockEventType.PURCHASE));
    assertThat(stockEvent.getBuyerName(), is("some user"));
    assertThat(stockEvent.getNumberSold(), is(1));
    assertThat(stockEvent.toString(), is("Event: PURCHASE; BuyerName: some user; NumberSold: 1;"));
  }

}