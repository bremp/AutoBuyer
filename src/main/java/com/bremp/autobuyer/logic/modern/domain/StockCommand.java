package com.bremp.autobuyer.logic.modern.domain;

import java.util.Objects;

public class StockCommand {

  private final String content;

  public StockCommand(String content) {
    this.content = content;
  }

  public static StockCommand buy(int price, int number)
  {
    return new StockCommand(String.format("Command: BUY; Price: %d; Number: %d", price, number));
  }

  public static StockCommand none()
  {
    return new StockCommand("");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StockCommand that = (StockCommand) o;
    return Objects.equals(content, that.content);
  }

  @Override
  public int hashCode() {
    return Objects.hash(content);
  }

  @Override
  public String toString() {
    return "StockCommand{" +
        "content='" + content + '\'' +
        '}';
  }

}
