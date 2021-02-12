package com.bremp.autobuyer.logic.modern.domain;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

class StockCommandTest {

  @Test
  void buy_command_is_of_appropriate_content() {
    StockCommand command = StockCommand.buy(123, 10);

    assertThat(command.toString(), is("StockCommand{content='Command: BUY; Price: 123; Number: 10'}"));
  }

  @Test
  void two_commands_are_equal_if_their_contents_match() {
    StockCommand command1 = StockCommand.buy(123, 10);
    StockCommand command2 = StockCommand.buy(123, 10);

    boolean areEqual1 = command1.equals(command2);
    boolean areEqual2 = command1.hashCode() == command2.hashCode();

    assertThat(areEqual1, is(true));
    assertThat(areEqual2, is(true));
  }

}