package org.sm.mw.order.delivery;

import java.math.BigDecimal;

public class FreeDeliveryPolicy implements DeliveryPolicy {

  @Override
  public DeliverDetails deliver(Deliverable deliverable) {
    return new DeliverDetails(BigDecimal.ZERO);
  }
}
