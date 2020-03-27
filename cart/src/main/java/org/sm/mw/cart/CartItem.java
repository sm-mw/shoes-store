package org.sm.mw.cart;

public class CartItem {

    private final Integer productId;
    private final Integer amount;

    public CartItem(Integer productId, Integer amount) {
        this.productId = productId;
        this.amount = amount;
    }

    public Integer productId() {
        return productId;
    }

    public Integer amount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CartItem cartItem = (CartItem) o;

        if (!productId.equals(cartItem.productId)) return false;
        return amount.equals(cartItem.amount);
    }

    @Override
    public int hashCode() {
        int result = productId.hashCode();
        result = 31 * result + amount.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "productId=" + productId +
                ", amount=" + amount +
                '}';
    }
}
