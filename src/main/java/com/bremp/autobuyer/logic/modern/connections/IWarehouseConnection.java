package com.bremp.autobuyer.logic.modern.connections;

public interface IWarehouseConnection {

  IStockItemConnection connectToItem(String itemId);

}
