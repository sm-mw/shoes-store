package org.sm.mw.order;

import org.sm.mw.cart.ApprovedItemSnapshot;
import org.sm.mw.cart.Cart;

import java.util.Collections;
import java.util.List;

public class Order {


    private List<ApprovedItemSnapshot> items = Collections.emptyList();

    public Order(Cart cart) {
        this.items = cart.approved();
    }

    public boolean create() {
        return !this.items().isEmpty();
    }

    List<ApprovedItemSnapshot> items() {
        return items;
    }
}
