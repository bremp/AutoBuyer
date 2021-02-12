package com.bremp.autobuyer.logic.old;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StockMessageTranslatorTest {

  @Mock
  IStockEventListener stockEventListener;

  @Test
  void notifies_stock_closes_when_close_message_received() {
    StockMessageTranslator sut = new StockMessageTranslator("Buyer");
    sut.addStockEventListener(stockEventListener);

    sut.processMessage("Event: CLOSE;");

    verify(stockEventListener, times(1)).itemClosed();
  }

  @Test
  void notifies_current_price_when_price_message_received() {
    StockMessageTranslator sut = new StockMessageTranslator("Buyer");
    sut.addStockEventListener(stockEventListener);

    sut.processMessage("Event: PRICE; CurrentPrice: 12; NumberInStock: 34");

    verify(stockEventListener, times(1)).currentPrice(12, 34);
  }

  @Test
  void notifies_item_purchased_by_the_buyer_when_purchase_message_received() {
    StockMessageTranslator sut = new StockMessageTranslator("Buyer");
    sut.addStockEventListener(stockEventListener);

    sut.processMessage("Event: PURCHASE; BuyerName: Buyer; NumberSold: 1");

    verify(stockEventListener, times(1)).itemPurchased(1, PurchaseSource.FROM_BUYER);
  }

  @Test
  void notifies_item_purchased_by_other_buyer_when_purchase_message_received() {
    StockMessageTranslator sut = new StockMessageTranslator("Buyer");
    sut.addStockEventListener(stockEventListener);

    sut.processMessage("Event: PURCHASE; BuyerName: OtherBuyer; NumberSold: 1");

    verify(stockEventListener, times(1)).itemPurchased(1, PurchaseSource.FROM_OTHER_BUYER);
  }

  @Test
  void throws_when_incorrect_message_received() {
    StockMessageTranslator sut = new StockMessageTranslator("Buyer");
    sut.addStockEventListener(stockEventListener);

    assertThrows(IllegalArgumentException.class, () -> {
      sut.processMessage("incorrect message");
    });
  }

  @Test
  void throws_when_message_of_unknown_type_received() {
    StockMessageTranslator sut = new StockMessageTranslator("Buyer");
    sut.addStockEventListener(stockEventListener);

    assertThrows(IllegalArgumentException.class, () -> {
      sut.processMessage("Event: UNKNOWN;");
    });
  }

}