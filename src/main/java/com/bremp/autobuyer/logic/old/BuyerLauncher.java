package com.bremp.autobuyer.logic.old;

public class BuyerLauncher {

  private final IWarehouse warehouse;
  private final IBuyerPortfolio portfolio;

  public BuyerLauncher(IWarehouse warehouse, IBuyerPortfolio portfolio) {
    this.warehouse = warehouse;
    this.portfolio = portfolio;
  }

  public void startBuying(String newItemId, int newItemMaximumPrice, int numberToBuy) {
    IStockItem stockItem = warehouse.getStockItemFor(newItemId);
    Buyer buyer = new Buyer(newItemId, newItemMaximumPrice, numberToBuy, stockItem);
    stockItem.addStockEventListener(buyer);
    portfolio.addBuyer(buyer);
  }

}
