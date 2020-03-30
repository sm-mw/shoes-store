package org.sm.mw.order

import org.sm.mw.cart.ApprovedItemSnapshot
import org.sm.mw.cart.Cart
import org.sm.mw.cart.CartItem
import org.sm.mw.cart.TimeProvider
import org.sm.mw.cart.snapshot.CartItemSnapshot
import spock.lang.Specification

import java.time.Instant

class OrderTest extends Specification {


    TimeProvider timeProvider = Mock(TimeProvider)
    def fixedTime = Instant.parse("2020-03-26T18:35:24.00Z")

    def setup() {
        timeProvider.now() >> fixedTime
    }


    def "should create order when cart was approved"() {
        given:
        def cart = Mock(Cart)
        cart.approved() >> [new ApprovedItemSnapshot(new CartItemSnapshot(1, 1, 1.0, 0.5), 1)]
        def order = new Order(cart)

        when:
        boolean created = order.create()

        then:
        created
    }
}
