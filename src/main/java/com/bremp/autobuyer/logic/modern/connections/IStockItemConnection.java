package com.bremp.autobuyer.logic.modern.connections;

import java.util.function.Consumer;

public interface IStockItemConnection {

  Consumer<String> messageReceived();
  void sendMessage(String message);

}
