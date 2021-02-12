package com.bremp.autobuyer.logic.modern.domain;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

class BuyerTest {

  @Test
  void new_buyer_is_in_joining_state() {
    var buyer = new Buyer("name", 34, 1);

    shouldEqual(buyer, BuyerState.JOINING, 0, 0, 0);
  }

  @Test
  void closes_when_item_closes() {
    var buyer = createJoiningBuyer(100, 1);

    StockCommand command = buyer.process(StockEvent.close());

    assertThat(command.toString(), is(StockCommand.none().toString()));
    shouldEqual(buyer, BuyerState.CLOSED, 0, 0, 0);
  }

  @Test
  void buyer_does_not_buy_when_price_event_with_too_high_price_arrives() {
    var buyer = createJoiningBuyer(10, 1);

    StockCommand command = buyer.process(StockEvent.price(20, 5));

    assertThat(command.toString(), is(StockCommand.none().toString()));
    shouldEqual(buyer, BuyerState.MONITORING, 20, 5, 0);
  }

  @Test
  void buyer_buys_when_price_event_with_appropriate_price_arrives() {
    var buyer = createJoiningBuyer(50, 1);

    StockCommand command = buyer.process(StockEvent.price(10, 5));

    assertThat(command.toString(), is(StockCommand.buy(10, 1).toString()));
    shouldEqual(buyer, BuyerState.BUYING, 10, 5, 0);
  }

  @Test
  void buyer_attempts_to_buy_maximum_amount_available() {
    var buyer = createJoiningBuyer(50, 10);

    StockCommand command = buyer.process(StockEvent.price(10, 5));

    assertThat(command.toString(), is(StockCommand.buy(10, 5).toString()));
    shouldEqual(buyer, BuyerState.BUYING, 10, 5, 0);
  }

  @Test
  void buyer_does_not_react_to_a_purchase_event_related_to_another_buyer() {
    var buyer = createMonitoringBuyer("Buyer", 100, 10);

    StockCommand command = buyer.process(StockEvent.purchase("Some other buyer", 1));

    assertThat(command.toString(), is(StockCommand.none().toString()));
    assertThat(buyer.getSnapshot().getState(), is(BuyerState.MONITORING));
    assertThat(buyer.getSnapshot().getBoughtSoFar(), is(0));
  }

  @Test
  void buyer_updates_items_bought_so_far_when_purchase_event_with_the_same_user_name_arrives() {
    var buyer = createMonitoringBuyer("name", 10, 10);

    StockCommand command = buyer.process(StockEvent.purchase("name", 1));

    assertThat(command.toString(), is(StockCommand.none().toString()));
    assertThat(buyer.getSnapshot().getState(), is(BuyerState.MONITORING));
    assertThat(buyer.getSnapshot().getBoughtSoFar(), is(1));
    assertThat(buyer.getSnapshot().getNumberInStock(), is(9));
  }

  @Test
  void buyer_closes_when_it_buys_enough_items() {
    var buyer = createMonitoringBuyer("Buyer", 100, 5);

    StockCommand command = buyer.process(StockEvent.purchase("Buyer", 5));

    assertThat(command.toString(), is(StockCommand.none().toString()));
    assertThat(buyer.getSnapshot().getState(), is(BuyerState.CLOSED));
  }

  @Test
  void closed_buyer_does_not_react_to_further_messages() {
    var buyer = createClosedBuyer(10);

    StockCommand command = buyer.process(StockEvent.price(10, 10));

    assertThat(command.toString(), is(StockCommand.none().toString()));
    assertThat(buyer.getSnapshot().getState(), is(BuyerState.CLOSED));
  }

  private Buyer createClosedBuyer(int maximumPrice) {
    var buyer = new Buyer("Buyer", maximumPrice, 1);
    buyer.process(StockEvent.close());
    return buyer;
  }

  private Buyer createMonitoringBuyer(String buyerName, int numberInStock, int numberToBuy) {
    var buyer = new Buyer(buyerName, 100, numberToBuy);
    buyer.process(StockEvent.price(200, numberInStock));
    return buyer;
  }

  private Buyer createJoiningBuyer(int maximumPrice, int numberToBuy) {
    return new Buyer("", maximumPrice, numberToBuy);
  }

  void shouldEqual(Buyer buyer, BuyerState state, int currentPrice, int numberInStock,
      int boughtSoFar) {

    assertThat(buyer.getSnapshot().getState(), is(state));
    assertThat(buyer.getSnapshot().getCurrentPrice(), is(currentPrice));
    assertThat(buyer.getSnapshot().getNumberInStock(), is(numberInStock));
    assertThat(buyer.getSnapshot().getBoughtSoFar(), is(boughtSoFar));
  }

}