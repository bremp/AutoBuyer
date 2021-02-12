package com.bremp.autobuyer.logic.old;

public interface IStockEventListener {

  void currentPrice(int price, int numberInStock);
  void itemPurchased(int numberSold, PurchaseSource purchaseSource);
  void itemClosed();

}
