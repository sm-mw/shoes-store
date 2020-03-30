package org.sm.mw.cart.discount


import spock.lang.Specification

class WholeCartDiscountPolicySpec extends Specification {

    def "should apply discount to any discountable item "() {
        given:
        def discountPolicy = new WholeCartDiscountPolicy(new BigDecimal(discountAmount))

        when:
        def value = discountPolicy.discount(new TestDiscountable(BigDecimal.valueOf(price)))

        then:
        value == new BigDecimal(expectedPrice)

        where:
        discountAmount | price | expectedPrice
        "10"           | 100   | "90"
        "15"           | 150   | "127.5"
        "0"            | 123   | "123"

    }
}



