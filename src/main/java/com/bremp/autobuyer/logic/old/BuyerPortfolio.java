package com.bremp.autobuyer.logic.old;

import java.util.ArrayList;
import java.util.List;

public class BuyerPortfolio implements IBuyerPortfolio {

  private final List<IPortfolioListener> listeners = new ArrayList<>();
  private final List<Buyer> buyers = new ArrayList<>();

  @Override
  public void addBuyer(Buyer buyer) {
    buyers.add(buyer);
    for (IPortfolioListener listener : listeners) {
      listener.buyerAdded(buyer);
    }
  }

  public void addPortfolioListener(IPortfolioListener listener) {
    listeners.add(listener);
  }
}
