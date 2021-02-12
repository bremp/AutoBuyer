package com.bremp.autobuyer.logic.old;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BuyerTest {

  @Mock
  IBuyerListener buyerListener;
  @Mock
  IStockItem stockItem;
  @Captor
  ArgumentCaptor<BuyerSnapshot> snapshotCaptor;

  @Test
  void closes_when_item_closes() {
    Buyer sut = new Buyer("ItemId", 10, 1, null);
    sut.addBuyerListener(buyerListener);

    sut.itemClosed();

    verify(buyerListener, times(1)).buyerStateChanged(snapshotCaptor.capture());
    BuyerSnapshot snapshot = snapshotCaptor.getValue();
    assertThat(snapshot.getCurrentPrice(), is(0));
    assertThat(snapshot.getNumberInStock(), is(0));
    assertThat(snapshot.getBoughtSoFar(), is(0));
    assertThat(snapshot.getState(), is(BuyerState.CLOSED));
  }

  @Test
  void buyer_does_not_buy_when_price_event_with_too_high_price_arrives() {
    Buyer sut = new Buyer("ItemId", 10, 1, null);
    sut.addBuyerListener(buyerListener);

    sut.currentPrice(20, 5);

    verify(buyerListener, times(1)).buyerStateChanged(snapshotCaptor.capture());
    BuyerSnapshot snapshot = snapshotCaptor.getValue();
    assertThat(snapshot.getCurrentPrice(), is(20));
    assertThat(snapshot.getNumberInStock(), is(5));
    assertThat(snapshot.getBoughtSoFar(), is(0));
    assertThat(snapshot.getState(), is(BuyerState.MONITORING));
  }

  @Test
  void buyer_buys_when_price_event_with_appropriate_price_arrives() {
    Buyer sut = new Buyer("ItemId", 50, 1, stockItem);
    sut.addBuyerListener(buyerListener);

    sut.currentPrice(10, 5);

    verify(buyerListener, times(1)).buyerStateChanged(snapshotCaptor.capture());
    verify(stockItem, times(1)).buy(10, 1);
    BuyerSnapshot snapshot = snapshotCaptor.getValue();
    assertThat(snapshot.getCurrentPrice(), is(10));
    assertThat(snapshot.getNumberInStock(), is(5));
    assertThat(snapshot.getBoughtSoFar(), is(0));
    assertThat(snapshot.getState(), is(BuyerState.BUYING));
  }

  @Test
  void buyer_attempts_to_buy_maximum_amount_available() {
    Buyer sut = new Buyer("ItemId", 50, 10, stockItem);

    sut.currentPrice(10, 5);

    verify(stockItem, times(1)).buy(10, 5);
  }

  @Test
  void buyer_does_not_react_to_a_purchase_event_related_to_another_buyer() {
    Buyer sut = new Buyer("ItemId", 10, 1, stockItem);

    sut.currentPrice(100, 5);

    sut.itemPurchased(1, PurchaseSource.FROM_OTHER_BUYER);
  }

  @Test
  void buyer_updates_items_bought_so_far_when_purchase_event_with_the_same_user_name_arrives() {
    Buyer sut = new Buyer("ItemId", 10, 10, null);
    sut.addBuyerListener(buyerListener);

    sut.currentPrice(50, 10);
    sut.itemPurchased(1, PurchaseSource.FROM_BUYER);

    verify(buyerListener, times(2)).buyerStateChanged(snapshotCaptor.capture());
    BuyerSnapshot snapshot = snapshotCaptor.getValue();
    assertThat(snapshot.getCurrentPrice(), is(50));
    assertThat(snapshot.getNumberInStock(), is(9));
    assertThat(snapshot.getBoughtSoFar(), is(1));
    assertThat(snapshot.getState(), is(BuyerState.MONITORING));
  }

  @Test
  void buyer_closes_when_it_buys_enough_items() {
    Buyer sut = new Buyer("ItemId", 10, 5, null);
    sut.addBuyerListener(buyerListener);

    sut.currentPrice(50, 10);
    sut.itemPurchased(5, PurchaseSource.FROM_BUYER);

    verify(buyerListener, times(2)).buyerStateChanged(snapshotCaptor.capture());
    BuyerSnapshot snapshot = snapshotCaptor.getValue();
    assertThat(snapshot.getCurrentPrice(), is(50));
    assertThat(snapshot.getNumberInStock(), is(5));
    assertThat(snapshot.getBoughtSoFar(), is(5));
    assertThat(snapshot.getState(), is(BuyerState.CLOSED));
  }

  @Test
  void closed_buyer_does_not_react_to_further_messages() {
    Buyer sut = new Buyer("ItemId", 10, 10, stockItem);
    sut.addBuyerListener(buyerListener);

    sut.itemClosed();
    sut.currentPrice(10, 10);
  }

}