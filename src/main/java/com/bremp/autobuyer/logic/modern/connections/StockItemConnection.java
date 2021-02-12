package com.bremp.autobuyer.logic.modern.connections;

import java.util.function.Consumer;

public class StockItemConnection implements IStockItemConnection {

  @Override
  public Consumer<String> messageReceived() {
    return null;
  }

  @Override
  public void sendMessage(String message) {
    // Send the message through a 3rd party SDK
  }
}
