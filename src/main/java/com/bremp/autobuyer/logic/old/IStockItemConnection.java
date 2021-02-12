package com.bremp.autobuyer.logic.old;

public interface IStockItemConnection {

  // event Action<string> MessageReceived;
  String getBuyerName();
  void sendMessage(String message);

}
