package org.sm.mw.order

import org.sm.mw.cart.ApprovedItemSnapshot
import org.sm.mw.cart.snapshot.CartItemSnapshot
import spock.lang.Specification

class OrderTest extends Specification {

    def "should create order"() {
        given:
        def approved = [new ApprovedItemSnapshot(new CartItemSnapshot(1, 1, 1.0, 0.5), 1)]

        when:
        def order = Order.create(approved)

        then:
        order.itemsCount() > 0
    }

    def "should throw exception when items is empty"() {
        when:
        Order.create([])

        then:
        OrderCreationException ex = thrown()
        ex.message() == "Items can not be empty or null."
    }
}
