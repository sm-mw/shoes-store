package org.sm.mw.order.delivery;

public class PaidDeliveryPolicy implements DeliveryPolicy {

    private DeliveryProvider deliveryProvider;

    public PaidDeliveryPolicy(DeliveryProvider deliveryProvider) {
        this.deliveryProvider = deliveryProvider;
    }

    @Override
    public DeliverDetails deliver(Deliverable deliverable) {
        return new DeliverDetails(deliveryProvider.getDeliveryPrice());
    }
}
