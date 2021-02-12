package com.bremp.autobuyer.logic.old;

public class StockItemConnection implements IStockItemConnection {

  // public event Action<string> MessageReceived;
  private final String buyerName;

  public StockItemConnection(String buyerName) {
    this.buyerName = buyerName;
  }

  @Override
  public String getBuyerName() {
    return this.buyerName;
  }

  @Override
  public void sendMessage(String message) {
    // Send the message through a 3rd party SDK.
  }
}
