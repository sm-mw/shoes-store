package org.sm.mw.cart.discount;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.sm.mw.cart.Discountable;

public class WholeCartDiscountPolicy implements DiscountPolicy {

    private final BigDecimal percentage;

    public WholeCartDiscountPolicy(BigDecimal percentage) {
        this.percentage = percentage;
    }

    @Override
    public BigDecimal discount(Discountable discountable) {
        BigDecimal divide = percentage.divide(new BigDecimal("100.00"), 2, RoundingMode.UNNECESSARY);
        return discountable.price().subtract(discountable.price().multiply(divide)).setScale(2, RoundingMode.HALF_UP);
    }
}
