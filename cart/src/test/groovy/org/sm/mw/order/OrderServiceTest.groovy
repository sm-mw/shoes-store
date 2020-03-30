package org.sm.mw.order

import org.sm.mw.cart.Cart
import org.sm.mw.cart.TimeProvider
import spock.lang.Specification

import java.time.Instant

class OrderServiceTest extends Specification {

    TimeProvider timeProvider = Mock(TimeProvider)
    def fixedTime = Instant.parse("2020-03-26T18:35:24.00Z")

    void setup() {
        timeProvider.now() >> fixedTime
    }

    def "should create order when cart was approved"() {
        given:
        def cart = new Cart(timeProvider)
        def service = new OrderService()

        when:
        boolean result = service.createOrder(cart)

        then:
        result
    }
}
