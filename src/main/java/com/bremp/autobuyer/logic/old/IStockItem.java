package com.bremp.autobuyer.logic.old;

public interface IStockItem {

  void buy(int price, int numberToBuy);
  void addStockEventListener(IStockEventListener listener);

}
