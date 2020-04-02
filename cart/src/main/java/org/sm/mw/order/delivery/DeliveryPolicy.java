package org.sm.mw.order.delivery;

public interface DeliveryPolicy {

  DeliverDetails deliver(Deliverable deliverable);

}
