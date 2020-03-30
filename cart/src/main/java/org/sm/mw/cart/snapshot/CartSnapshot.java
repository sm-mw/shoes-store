package org.sm.mw.cart.snapshot;

import java.util.Collections;
import java.util.List;

public class CartSnapshot {

    private final List<CartItemSnapshot> cartItemSnapshotList;

    public CartSnapshot(List<CartItemSnapshot> cartItemSnapshotList) {
        this.cartItemSnapshotList = Collections.unmodifiableList(cartItemSnapshotList);
    }

    public List<CartItemSnapshot> items() {
        return this.cartItemSnapshotList;
    }
}
