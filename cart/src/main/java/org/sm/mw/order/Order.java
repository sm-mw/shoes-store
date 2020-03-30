package org.sm.mw.order;

import org.sm.mw.cart.Cart;
import org.sm.mw.cart.CartItem;
import org.sm.mw.cart.ProductStockSnapshot;

import java.util.Collections;
import java.util.Map;

public class Order {


    private Map<CartItem, ProductStockSnapshot> items = Collections.emptyMap();

    public Order(Cart cart) {
        this.items =  cart.approvedItems();
    }

    public boolean create() {
        return false;
    }

    public Map<CartItem, ProductStockSnapshot> items() {
        return items;
    }
}
