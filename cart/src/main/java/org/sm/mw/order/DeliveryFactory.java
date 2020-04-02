package org.sm.mw.order;

import org.sm.mw.order.delivery.DeliveryPolicy;
import org.sm.mw.order.delivery.DeliveryProvider;

public class DeliveryFactory {

    public static DeliveryPolicy resolve(DeliveryProvider deliveryProvider) {
        if (deliveryProvider == DeliveryProvider.COURIER) {
            return null; // new CourierDeliveryPolicy(deliveryProvider.getDeliveryPrice());
        } else if (deliveryProvider == DeliveryProvider.PARCEL_LOCKER) {
            return null; // new ParcelLockerDeliveryPolicy(deliveryProvider.getDeliveryPrice());
        } else {
            return null; //new PostOfficeDeliveryPolicy(deliveryProvider.getDeliveryPrice());
        }
    }

}
