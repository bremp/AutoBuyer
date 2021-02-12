package com.bremp.autobuyer.logic.old;

import java.util.ArrayList;
import java.util.List;

public class Buyer implements IStockEventListener {

  private final List<IBuyerListener> listeners = new ArrayList<>();
  private final int maximumPrice;
  private final int numberToBuy;
  private final IStockItem stockItem;
  private BuyerSnapshot snapshot;

  public Buyer(String itemId, int maximumPrice, int numberToBuy, IStockItem stockItem) {
    this.maximumPrice = maximumPrice;
    this.numberToBuy = numberToBuy;
    this.stockItem = stockItem;
    snapshot = BuyerSnapshot.joining(itemId);
  }

  public void addBuyerListener(IBuyerListener listener) {
    listeners.add(listener);
  }

  @Override
  public void currentPrice(int price, int numberInStock) {
    if (snapshot.getState() == BuyerState.CLOSED) {
      return;
    }
    if (price > maximumPrice) {
      snapshot = snapshot.monitoring(price, numberInStock);
    } else {
      int numToBuy = Math.min(numberInStock, this.numberToBuy);
      stockItem.buy(price, numToBuy);
      snapshot = snapshot.buying(price, numberInStock);
    }

    notifyChange();
  }

  @Override
  public void itemPurchased(int numberSold, PurchaseSource purchaseSource) {
    if (purchaseSource == PurchaseSource.FROM_BUYER) {
      snapshot = snapshot.bought(numberSold);
      if (snapshot.getBoughtSoFar() >= numberToBuy) {
        snapshot = snapshot.closed();
      }
      notifyChange();
    }
  }

  @Override
  public void itemClosed() {
    snapshot = snapshot.closed();
    notifyChange();
  }

  private void notifyChange() {
    for (IBuyerListener listener : listeners) {
      listener.buyerStateChanged(snapshot);
    }
  }

}
