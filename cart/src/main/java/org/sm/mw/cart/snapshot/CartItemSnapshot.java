package org.sm.mw.cart.snapshot;

import java.math.BigDecimal;

public class CartItemSnapshot {
    private final Integer productId;
    private final Integer amount;
    private final BigDecimal price;
    private final BigDecimal promoPrice;

    public CartItemSnapshot(Integer productId, Integer amount, BigDecimal price, BigDecimal promoPrice) {
        this.productId = productId;
        this.amount = amount;
        this.price = price;
        this.promoPrice = promoPrice;
    }

    public BigDecimal promoPrice() {
        return promoPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CartItemSnapshot that = (CartItemSnapshot) o;

        if (!productId.equals(that.productId)) return false;
        if (!amount.equals(that.amount)) return false;
        if (!price.equals(that.price)) return false;
        return promoPrice != null ? promoPrice.equals(that.promoPrice) : that.promoPrice == null;
    }

    @Override
    public int hashCode() {
        int result = productId.hashCode();
        result = 31 * result + amount.hashCode();
        result = 31 * result + price.hashCode();
        result = 31 * result + (promoPrice != null ? promoPrice.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CartItemSnapshot{" +
            "productId=" + productId +
            ", amount=" + amount +
            ", price=" + price +
            ", promoPrice=" + promoPrice +
            '}';
    }
}
