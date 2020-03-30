package org.sm.mw.cart.discount;

import java.math.BigDecimal;
import org.sm.mw.cart.Discountable;

public class TestDiscountable implements Discountable {

    private final BigDecimal price;

    TestDiscountable(BigDecimal price) {
        this.price = price;
    }

    @Override
    public BigDecimal price() {
        return this.price;
    }

    @Override
    public int id() {
        return 0;
    }
}
