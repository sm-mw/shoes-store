package org.sm.mw.order

import org.sm.mw.cart.ApprovedItemSnapshot
import org.sm.mw.cart.snapshot.CartItemSnapshot
import spock.lang.Specification

class OrderSpec extends Specification {

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

    def "should apply free delivery policy when order amount is more than 100"() {
        given:
        def order = Order.create(apprivedItemsSnapshot)

        when:
        def applyResult = order.applyDelivery()

        then:
        applyResult.isSuccessful()

        when:
        def deliverResult = order.deliverDetails()

        then:
        deliverResult.deliveryPrice() == deliveryPrice

        where:
        apprivedItemsSnapshot                                | deliveryPrice
        [new ApprovedItemSnapshot(
                new CartItemSnapshot(1, 1, 1.0, 0.5), 1),
         new ApprovedItemSnapshot(
                 new CartItemSnapshot(2, 1, 120.0, 120.0), 1),
         new ApprovedItemSnapshot(
                 new CartItemSnapshot(3, 1, 30.0, 20.0), 1)] | BigDecimal.ZERO
        [new ApprovedItemSnapshot(
                new CartItemSnapshot(1, 1, 1.0, 0.5), 1),
         new ApprovedItemSnapshot(
                 new CartItemSnapshot(2, 1, 2.0, 2.0), 1),
         new ApprovedItemSnapshot(
                 new CartItemSnapshot(3, 1, 30.0, 20.0), 1)] | BigDecimal.valueOf(100)
    }
}
