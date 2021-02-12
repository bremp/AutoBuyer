package com.bremp.autobuyer.logic.old;

public class StockItem implements IStockItem {

  private static final String BUY_COMMAND_FORMAT = "Command: BUY; Price: {0}; Number: {1}";

  private final IStockItemConnection connection;
  private final StockMessageTranslator translator;

  public StockItem(IStockItemConnection connection) {
    this.connection = connection;
    this.translator = new StockMessageTranslator(connection.getBuyerName());
    // _connection.MessageReceived += TranslateMessage;
  }

  @Override
  public void buy(int price, int numberToBuy) {
    connection.sendMessage(String.format(BUY_COMMAND_FORMAT, price, numberToBuy));
  }

  @Override
  public void addStockEventListener(IStockEventListener listener) {
    translator.addStockEventListener(listener);
  }

  private void translateMessage(String message) {
    translator.processMessage(message);
  }

}
