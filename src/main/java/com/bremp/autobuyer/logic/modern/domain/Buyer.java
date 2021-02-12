package com.bremp.autobuyer.logic.modern.domain;

public class Buyer {

  private final String buyerName;
  private final int maximumPrice;
  private final int numberToBuy;
  private BuyerSnapshot snapshot;

  public Buyer(String buyerName, int maximumPrice, int numberToBuy) {
    this.buyerName = buyerName;
    this.maximumPrice = maximumPrice;
    this.numberToBuy = numberToBuy;
    this.snapshot = BuyerSnapshot.joining();
  }

  public String getBuyerName() {
    return buyerName;
  }

  public int getMaximumPrice() {
    return maximumPrice;
  }

  public int getNumberToBuy() {
    return numberToBuy;
  }

  public BuyerSnapshot getSnapshot() {
    return snapshot;
  }

  public StockCommand process(StockEvent stockEvent) {
    if (snapshot.getState() == BuyerState.CLOSED) {
      return StockCommand.none();
    }

    switch (stockEvent.getType()) {
      case PURCHASE:
        return processPurchaseEvent(stockEvent.getBuyerName(), stockEvent.getNumberSold());
      case PRICE:
        return processPriceEvent(stockEvent.getCurrentPrice(), stockEvent.getNumberInStock());
      case CLOSE:
        return processCloseEvent();
      default:
        throw new UnsupportedOperationException();
    }
  }

  private StockCommand processPurchaseEvent(String buyerName, int numberSold) {
    if (this.buyerName.equals(buyerName)) {
      snapshot = snapshot.bought(numberSold);
      if (snapshot.getBoughtSoFar() >= numberToBuy) {
        snapshot = snapshot.closed();
      }
    }
    return StockCommand.none();
  }

  private StockCommand processPriceEvent(int currentPrice, int numberInStock) {
    if (currentPrice > maximumPrice) {
      snapshot = snapshot.monitoring(currentPrice, numberInStock);
      return StockCommand.none();
    }

    snapshot = snapshot.buying(currentPrice, numberInStock);

    int numToBuy = Math.min(numberInStock, this.numberToBuy);
    return StockCommand.buy(currentPrice, numToBuy);
  }

  private StockCommand processCloseEvent() {
    snapshot = snapshot.closed();
    return StockCommand.none();
  }

}
