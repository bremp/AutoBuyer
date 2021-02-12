package com.bremp.autobuyer.logic.old;

public class Warehouse implements IWarehouse {

  private final IWarehouseConnection connection;
  private final String buyerName;

  public Warehouse(IWarehouseConnection connection, String buyerName) {
    this.connection = connection;
    this.buyerName = buyerName;
  }

  @Override
  public IStockItem getStockItemFor(String itemId) {
    return new StockItem(connection.connectToItem(itemId, buyerName));
  }
}
