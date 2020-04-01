package org.sm.mw.order;

import org.sm.mw.cart.ApprovedItemSnapshot;

import java.util.List;

public class Order {

    private List<ApprovedItemSnapshot> items;

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
}
