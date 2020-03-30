package org.sm.mw.cart;

import org.sm.mw.cart.snapshot.CartItemSnapshot;

public class ApprovedItemSnapshot {

    private final CartItemSnapshot cartItem;
    private final Integer approvedAmount;

    public ApprovedItemSnapshot(CartItemSnapshot cartItem, Integer approvedAmount) {
        this.cartItem = cartItem;
        this.approvedAmount = approvedAmount;
    }

    public CartItemSnapshot cartItem() {
        return cartItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApprovedItemSnapshot that = (ApprovedItemSnapshot) o;

        if (!cartItem.equals(that.cartItem)) return false;
        return approvedAmount.equals(that.approvedAmount);
    }

    @Override
    public int hashCode() {
        int result = cartItem.hashCode();
        result = 31 * result + approvedAmount.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ApprovedItem{" +
                "cartItem=" + cartItem +
                ", approvedAmount=" + approvedAmount +
                '}';
    }
}
