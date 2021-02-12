package com.bremp.autobuyer.logic.old;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BuyerPortfolioTest {

  @Mock
  IPortfolioListener portfolioListener;

  @Test
  void notifies_listeners_of_new_buyers() {
    BuyerPortfolio sut = new BuyerPortfolio();
    sut.addPortfolioListener(portfolioListener);
    Buyer buyer = new Buyer("ItemId", 10, 1, null);

    sut.addBuyer(buyer);

    verify(portfolioListener, times(1)).buyerAdded(buyer);
  }

}