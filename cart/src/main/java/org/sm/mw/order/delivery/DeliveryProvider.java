package org.sm.mw.order.delivery;

import java.math.BigDecimal;

public enum DeliveryProvider {

    COURIER(BigDecimal.valueOf(15)),
    PARCEL_LOCKER(BigDecimal.valueOf(9)),
    POST_OFFICE(BigDecimal.valueOf(20));

    private BigDecimal deliveryPrice;

    DeliveryProvider(BigDecimal deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public BigDecimal getDeliveryPrice() {
        return deliveryPrice;
    }
}
