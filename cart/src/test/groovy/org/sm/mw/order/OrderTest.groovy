package org.sm.mw.order

import org.sm.mw.cart.Cart
import spock.lang.Specification

class OrderTest extends Specification {

    def "should create order when cart was approved"() {
        given:
        def cart = new Cart()
        def order = new Order(cart)

        when:
        boolean created = order.create()

        then:
        created
        order.items().size() > 0
    }
}
