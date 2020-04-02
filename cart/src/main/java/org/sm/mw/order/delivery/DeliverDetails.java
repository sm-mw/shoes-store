package org.sm.mw.order.delivery;

import java.math.BigDecimal;

public class DeliverDetails {

  private BigDecimal deliveryPrice;

  public DeliverDetails(BigDecimal deliveryPrice) {
    this.deliveryPrice = deliveryPrice;
  }

  public BigDecimal deliveryPrice()
  {
    return deliveryPrice;
  }
}
