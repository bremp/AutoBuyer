package com.bremp.autobuyer.logic.modern.connections;

import java.util.ArrayList;
import java.util.List;

public class WarehouseConnection implements IWarehouseConnection {

  private final List<StockItemConnection> connections = new ArrayList<>();

  @Override
  public IStockItemConnection connectToItem(String itemId) {
    StockItemConnection connection = new StockItemConnection();
    connections.add(connection);

    return connection;
  }
}
