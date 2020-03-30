package org.sm.mw.cart.discount;

import java.math.BigDecimal;
import org.sm.mw.cart.Discountable;

public interface DiscountPolicy {

    BigDecimal discount(Discountable discountable);

}
