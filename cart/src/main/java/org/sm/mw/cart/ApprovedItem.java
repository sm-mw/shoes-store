package org.sm.mw.cart;

public class ApprovedItem {

    private final CartItem cartItem;
    private final Integer approvedAmount;

    public ApprovedItem(CartItem cartItem, Integer approvedAmount) {
        this.cartItem = cartItem;
        this.approvedAmount = approvedAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApprovedItem that = (ApprovedItem) o;

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
