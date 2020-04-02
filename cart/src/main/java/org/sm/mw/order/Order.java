package org.sm.mw.order;

import org.sm.mw.cart.ApprovedItemSnapshot;
import org.sm.mw.commons.Result;
import org.sm.mw.order.delivery.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Order implements Deliverable {

    private List<ApprovedItemSnapshot> items;
    private DeliverDetails deliverDetails;

    private Order(List<ApprovedItemSnapshot> items) {
        this.items = items;
    }

    static Order create(List<ApprovedItemSnapshot> items) {
        if (items == null || items.isEmpty()) {
            throw new OrderCreationException();
        }
        return new Order(items);
    }

    int itemsCount() {
        return this.items.size();
    }

    Result applyDelivery(DeliveryProvider deliveryProvider) {
        BigDecimal itemsSum = itemsSum();

        if (itemsSum.compareTo(BigDecimal.valueOf(100.00)) > 0) {
            this.deliverDetails = new FreeDeliveryPolicy().deliver(this);
            return Result.success();
        } else if (itemsSum.compareTo(BigDecimal.valueOf(10.00)) > 0){
            this.deliverDetails = new PaidDeliveryPolicy(deliveryProvider).deliver(this);
            return Result.success();
        }

        return Result.failure();
    }

    // todo finish or delete
//    public OrderSnapshot summary(){
//        List<DeliverySnapshot> deliveries = new ArrayList<>();
//        if(itemsSum().compareTo(BigDecimal.valueOf(100.00)) > ) {
//            deliveries.add(new DeliverySnapshot(BigDecimal.ZERO, ))
//        }
//
//        return new OrderSnapshot(this.items)
//    }

    @Override
    public BigDecimal itemsSum() {
        return items.stream()
                    .map(item -> item.cartItem().promoPrice())
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
    }

    @Override
    public Result applyDelivery(DeliveryPolicy deliveryPolicy) {
        this.deliverDetails = deliveryPolicy.deliver(this);
        return Result.success();
    }

    @Override
    public DeliverDetails deliverDetails() {
        return this.deliverDetails;
    }

}
