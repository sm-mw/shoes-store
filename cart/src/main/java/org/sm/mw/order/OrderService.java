package org.sm.mw.order;

import org.sm.mw.cart.Cart;

public class OrderService {

    public boolean createOrder(Cart cart) {

        Order order = new Order(cart);
        return true;
    }
}
