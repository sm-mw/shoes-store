package org.sm.mw.order;

import org.sm.mw.cart.ApprovedItemSnapshot;
import org.sm.mw.commons.Result;
import org.sm.mw.order.delivery.DeliverDetails;
import org.sm.mw.order.delivery.Deliverable;
import org.sm.mw.order.delivery.FreeDeliveryPolicy;

import java.math.BigDecimal;
import java.util.List;

public class Order implements Deliverable {

    private List<ApprovedItemSnapshot> items;
    private DeliverDetails deliverDetails;

    private Order(List<ApprovedItemSnapshot> items) {
        this.items = items;
    }

    static Order create(List<ApprovedItemSnapshot> items) {
        if(items == null || items.isEmpty()){
            throw new OrderCreationException();
        }
        return new Order(items);
    }

    int itemsCount() {
        return this.items.size();
    }
    Result applyDelivery() {
        BigDecimal itemsSum = items.stream()
            .map(item -> item.cartItem().promoPrice())
            .reduce(BigDecimal::add)
            .orElse(BigDecimal.ZERO);

        if(itemsSum.compareTo(BigDecimal.valueOf(100.00)) > 0) {
            this.deliverDetails =  new FreeDeliveryPolicy().deliver(this);
            return Result.success();
        }
        return Result.failure();
    }

    public DeliverDetails deliverDetails(){
        return this.deliverDetails;

    }

}
