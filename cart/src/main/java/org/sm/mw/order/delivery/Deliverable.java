package org.sm.mw.order.delivery;

import org.sm.mw.commons.Result;

import java.math.BigDecimal;

public interface Deliverable {


    BigDecimal itemsSum();

    Result applyDelivery(DeliveryPolicy deliveryPolicy);

    DeliverDetails deliverDetails();
}
