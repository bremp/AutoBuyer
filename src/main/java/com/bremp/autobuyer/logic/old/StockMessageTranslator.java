package com.bremp.autobuyer.logic.old;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class StockMessageTranslator {

  private final List<IStockEventListener> listeners = new ArrayList<>();
  private final String buyerName;

  public StockMessageTranslator(String buyerName) {
    this.buyerName = buyerName;
  }

  public void addStockEventListener(IStockEventListener listener) {
    listeners.add(listener);
  }

  public void processMessage(String message) {
    StockEvent stockEvent = StockEvent.from(message);
    switch (stockEvent.getType()) {

      case PRICE:
        notify(x -> x.currentPrice(stockEvent.getCurrentPrice(), stockEvent.getNumberInStock()));
        break;
      case PURCHASE:
        notify(x -> x.itemPurchased(stockEvent.getNumberSold(), getEventSource(stockEvent.getBuyerName())));
        break;
      case CLOSE:
        notify(IStockEventListener::itemClosed);
        break;

      default:
        throw new IllegalArgumentException();
    }
  }

  private PurchaseSource getEventSource(String buyerName) {
    return buyerName.equals(this.buyerName) ? PurchaseSource.FROM_BUYER : PurchaseSource.FROM_OTHER_BUYER;
  }

  private void notify(Consumer<IStockEventListener> action) {
    for (IStockEventListener listener : listeners) {
      action.accept(listener);
    }
  }

}
