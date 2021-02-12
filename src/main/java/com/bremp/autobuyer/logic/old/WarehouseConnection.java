package com.bremp.autobuyer.logic.old;

import java.util.ArrayList;
import java.util.List;

public class WarehouseConnection implements IWarehouseConnection {

  private final List<StockItemConnection> connections = new ArrayList<>();

  @Override
  public IStockItemConnection connectToItem(String itemId, String buyerName) {
    StockItemConnection connection = new StockItemConnection(buyerName);
    connections.add(connection);

    return connection;
  }
}
