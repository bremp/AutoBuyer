package com.bremp.autobuyer.logic.old;

public interface IWarehouseConnection {

  IStockItemConnection connectToItem(String itemId, String buyerName);

}
