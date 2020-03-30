package org.sm.mw.cart.discount

import spock.lang.Specification

class NoDiscountPolicySpec extends Specification {

    def "should not apply any discount"() {
        given:
        def discountPolicy = new NoDiscountPolicy()

        when:
        def value = discountPolicy.discount(new TestDiscountable(BigDecimal.valueOf(price)))

        then:
        value == new BigDecimal(expectedPrice)

        where:
        price | expectedPrice
        100   | "100.00"
        150   | "150.00"
        123   | "123.00"
    }


}
