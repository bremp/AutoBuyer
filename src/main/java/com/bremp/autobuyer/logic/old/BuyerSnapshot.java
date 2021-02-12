package com.bremp.autobuyer.logic.old;

public class BuyerSnapshot {

  private final String itemId;
  private final int currentPrice;
  private final int numberInStock;
  private final int boughtSoFar;
  private final BuyerState state;

  public BuyerSnapshot(String itemId, int currentPrice, int numberInStock, int boughtSoFar,
      BuyerState state) {
    this.itemId = itemId;
    this.currentPrice = currentPrice;
    this.numberInStock = numberInStock;
    this.boughtSoFar = boughtSoFar;
    this.state = state;
  }

  public String getItemId() {
    return itemId;
  }

  public int getCurrentPrice() {
    return currentPrice;
  }

  public int getNumberInStock() {
    return numberInStock;
  }

  public int getBoughtSoFar() {
    return boughtSoFar;
  }

  public BuyerState getState() {
    return state;
  }

  public static BuyerSnapshot joining(String itemId) {
    return new BuyerSnapshot(itemId, 0, 0, 0, BuyerState.JOINING);
  }

  public BuyerSnapshot monitoring(int currentPrice, int numberInStock) {
    return new BuyerSnapshot(itemId, currentPrice, numberInStock, boughtSoFar, BuyerState.MONITORING);
  }

  public BuyerSnapshot bought(int numberBought) {
    return new BuyerSnapshot(itemId, currentPrice, numberInStock - numberBought, boughtSoFar + numberBought, state);
  }

  public BuyerSnapshot closed() {
    return new BuyerSnapshot(itemId, currentPrice, numberInStock, boughtSoFar, BuyerState.CLOSED);
  }

  public BuyerSnapshot buying(int currentPrice, int numberInStock) {
    return new BuyerSnapshot(itemId, currentPrice, numberInStock, boughtSoFar, BuyerState.BUYING);
  }

}
