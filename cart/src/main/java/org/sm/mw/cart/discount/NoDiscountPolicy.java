package org.sm.mw.cart.discount;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.sm.mw.cart.Discountable;

public class NoDiscountPolicy implements DiscountPolicy {

    @Override
    public BigDecimal discount(Discountable discountable) {
        return discountable.price().setScale(2, RoundingMode.HALF_UP);
    }
}
