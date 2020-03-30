package org.sm.mw.cart;

import java.math.BigDecimal;
import org.sm.mw.cart.snapshot.CartItemSnapshot;

public class CartItem implements Discountable {

    private final Integer productId;
    private final Integer amount;
    private final BigDecimal price;

    public CartItem(Integer productId, Integer amount, BigDecimal price) {
        this.productId = productId;
        this.amount = amount;
        this.price = price;
    }

    public Integer productId() {
        return productId;
    }

    public Integer amount() {
        return amount;
    }

    @Override
    public BigDecimal price() {
        return this.price;
    }

    @Override
    public int id() {
        return productId;
    }

    public CartItemSnapshot snapshot(BigDecimal promoPrice) {
        return new CartItemSnapshot(productId, amount, price, promoPrice);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CartItem cartItem = (CartItem) o;

        if (!productId.equals(cartItem.productId)) return false;
        if (!amount.equals(cartItem.amount)) return false;
        return price.equals(cartItem.price);
    }

    @Override
    public int hashCode() {
        int result = productId.hashCode();
        result = 31 * result + amount.hashCode();
        result = 31 * result + price.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CartItem{" +
            "productId=" + productId +
            ", amount=" + amount +
            ", price=" + price +
            '}';
    }
}
