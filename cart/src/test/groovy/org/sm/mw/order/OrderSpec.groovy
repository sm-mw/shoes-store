package org.sm.mw.order

import org.sm.mw.cart.ApprovedItemSnapshot
import org.sm.mw.cart.snapshot.CartItemSnapshot
import org.sm.mw.order.delivery.DeliveryPolicy
import org.sm.mw.order.delivery.DeliveryProvider
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

    def "should apply delivery policy"() {
        given:
        def order = Order.create(approvedItemsSnapshot)

        when:
        def applyResult = order.applyDelivery(provider)

        then:
        applyResult.isSuccessful()

        when:
        def deliverResult = order.deliverDetails()

        then:
        deliverResult.deliveryPrice() == deliveryPrice

        where:
        approvedItemsSnapshot                                | deliveryPrice          | provider
        [new ApprovedItemSnapshot(
                new CartItemSnapshot(1, 1, 1.0, 0.5), 1),
         new ApprovedItemSnapshot(
                 new CartItemSnapshot(2, 1, 120.0, 120.0), 1),
         new ApprovedItemSnapshot(
                 new CartItemSnapshot(3, 1, 30.0, 20.0), 1)] | BigDecimal.ZERO        | DeliveryProvider.POST_OFFICE
        [new ApprovedItemSnapshot(
                new CartItemSnapshot(1, 1, 1.0, 0.5), 1),
         new ApprovedItemSnapshot(
                 new CartItemSnapshot(2, 1, 120.0, 120.0), 1),
         new ApprovedItemSnapshot(
                 new CartItemSnapshot(3, 1, 30.0, 20.0), 1)] | BigDecimal.ZERO        | DeliveryProvider.COURIER
        [new ApprovedItemSnapshot(
                new CartItemSnapshot(1, 1, 1.0, 0.5), 1),
         new ApprovedItemSnapshot(
                 new CartItemSnapshot(2, 1, 120.0, 120.0), 1),
         new ApprovedItemSnapshot(
                 new CartItemSnapshot(3, 1, 30.0, 20.0), 1)] | BigDecimal.ZERO        | DeliveryProvider.PARCEL_LOCKER
        [new ApprovedItemSnapshot(
                new CartItemSnapshot(1, 1, 1.0, 0.5), 1),
         new ApprovedItemSnapshot(
                 new CartItemSnapshot(2, 1, 2.0, 2.0), 1),
         new ApprovedItemSnapshot(
                 new CartItemSnapshot(3, 1, 30.0, 20.0), 1)] | BigDecimal.valueOf(15) | DeliveryProvider.COURIER
        [new ApprovedItemSnapshot(
                new CartItemSnapshot(1, 1, 1.0, 0.5), 1),
         new ApprovedItemSnapshot(
                 new CartItemSnapshot(2, 1, 2.0, 2.0), 1),
         new ApprovedItemSnapshot(
                 new CartItemSnapshot(3, 1, 30.0, 20.0), 1)] | BigDecimal.valueOf(20) | DeliveryProvider.POST_OFFICE
        [new ApprovedItemSnapshot(
                new CartItemSnapshot(1, 1, 1.0, 0.5), 1),
         new ApprovedItemSnapshot(
                 new CartItemSnapshot(2, 1, 2.0, 2.0), 1),
         new ApprovedItemSnapshot(
                 new CartItemSnapshot(3, 1, 30.0, 20.0), 1)] | BigDecimal.valueOf(9)  | DeliveryProvider.PARCEL_LOCKER
    }

    def "should fail delivery when order amount < 10"() {
        given:
        def approvedItems = [new ApprovedItemSnapshot(
                new CartItemSnapshot(1, 1, 1.0, 0.5), 1),
                             new ApprovedItemSnapshot(
                                     new CartItemSnapshot(2, 1, 2.0, 2.0), 1),
                             new ApprovedItemSnapshot(
                                     new CartItemSnapshot(3, 1, 3.0, 3.0), 1)]

        def order = Order.create(approvedItems)

        when:
        def applyResult = order.applyDelivery(DeliveryProvider.COURIER)

        then:
        !applyResult.isSuccessful()

    }

    def "should disable parcel locker when items amount is bigger than 5"() {
        given:
        def approvedItems = [new ApprovedItemSnapshot(
                new CartItemSnapshot(1, 1, 1.0, 0.5), 1),
                             new ApprovedItemSnapshot(
                                     new CartItemSnapshot(2, 1, 2.0, 2.0), 1),
                             new ApprovedItemSnapshot(
                                     new CartItemSnapshot(3, 1, 3.0, 3.0), 1),
                             new ApprovedItemSnapshot(
                                     new CartItemSnapshot(1, 1, 1.0, 0.5), 1),
                             new ApprovedItemSnapshot(
                                     new CartItemSnapshot(2, 1, 2.0, 2.0), 1),
                             new ApprovedItemSnapshot(
                                     new CartItemSnapshot(3, 1, 3.0, 3.0), 1)]

        when:

        def order = Order.create(approvedItems)
        def deliveryOptions = order.showDeliveryOptions()

        then:
        deliveryOptions.availableOptions() == [DeliveryProvider.COURIER, DeliveryProvider.POST_OFFICE]


    }
}
