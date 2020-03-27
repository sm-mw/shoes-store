package org.sm.mw.cart

import spock.lang.Specification

import java.time.Instant
import java.time.temporal.ChronoUnit

class CartSpec extends Specification {

    TimeProvider timeProvider = Mock(TimeProvider)
    def fixedTime = Instant.parse("2020-03-26T18:35:24.00Z")
    def item1 = new CartItem(1, 1)
    def item2 = new CartItem(2, 3)
    def item3 = new CartItem(3, 4)
    def item4 = new CartItem(4, 1)


    def setup() {
        timeProvider.now() >> fixedTime
    }

    def "should add new item to cart"() {
        expect:
        new Cart(timeProvider).addItem(item1).isSuccessful()
    }

    def "should add new item to cart if available in warehouse"() {
        expect:
        new Cart(timeProvider).addItem(item1).isSuccessful()

    }

    def "should remove added item from cart"() {
        given:
        def subject = new Cart(timeProvider)

        when:
        subject.addItem(item1)

        then:
        subject.removeItem(item1).isSuccessful()
        and:
        !subject.removeItem(item1).isSuccessful()
    }

    def "should not remove item when it is not present in cart"() {
        given:
        def subject = new Cart(timeProvider)

        when:
        def result = subject.removeItem(item1)

        then:
        !result.isSuccessful()
    }

    def "should check if cart is not abandoned"() {
        given:
        def subject = new Cart(timeProvider)

        when:
        timeProvider.now() >> fixedTime.plus(1, ChronoUnit.DAYS)
        def isAbandoned = subject.isAbandoned()

        then:
        !isAbandoned
    }

    def "should check if cart is abandoned after 3 days"() {

        given:
        TimeProvider multiTimeProvider = Mock(TimeProvider)
        multiTimeProvider.now() >>> [fixedTime, fixedTime.plus(3, ChronoUnit.DAYS)]
        def subject = new Cart(multiTimeProvider)

        when:
        def isAbandoned = subject.isAbandoned()

        then:
        isAbandoned
    }

    def "should activate abandoned cart when item added"() {
        given:
        TimeProvider multiTimeProvider = Mock(TimeProvider)
        multiTimeProvider.now() >>> [
                fixedTime.minus(3, ChronoUnit.DAYS),
                fixedTime,
                fixedTime.plus(1, ChronoUnit.DAYS)]
        def subject = new Cart(multiTimeProvider)

        when:
        subject.addItem()

        def isAbandoned = subject.isAbandoned()

        then:
        !isAbandoned
    }

    def "should activate abandoned cart when item removed"() {
        given:
        TimeProvider multiTimeProvider = Mock(TimeProvider)
        multiTimeProvider.now() >>> [
                fixedTime.minus(3, ChronoUnit.DAYS),
                fixedTime.minus(2, ChronoUnit.DAYS),
                fixedTime,
                fixedTime.plus(1, ChronoUnit.DAYS)]
        def subject = new Cart(multiTimeProvider)
        subject.addItem(item1)

        when:
        subject.removeItem(item1)

        def isAbandoned = subject.isAbandoned()

        then:
        !isAbandoned
    }


    def "should apply promo code when provided"() {
        given:
        def subject = new Cart(timeProvider)

        when:
        def result = subject.applyPromoCode()

        then:
        result.isSuccessful()
    }

    def "should approve cart when all products are available"() {
        given:
        def subject = new Cart(timeProvider)
        subject.addItem(item1)
        subject.addItem(item2)
        subject.addItem(item3)
        def productStockSnapshotOfItem1 = new ProductStockSnapshot(1, 1)
        def productStockSnapshotOfItem2 = new ProductStockSnapshot(2, 3)
        def productStockSnapshotOfItem3 = new ProductStockSnapshot(3, 3)
        StockStateSnapshot stockState = new StockStateSnapshot(
                List.of(productStockSnapshotOfItem1,
                        productStockSnapshotOfItem2,
                        productStockSnapshotOfItem3))

        when:
        def isApproved = subject.approve(stockState)

        then:
        isApproved.isSuccessful()
        !subject.addItem(item4).isSuccessful()
        !subject.removeItem(item4).isSuccessful()
        when:
        def approvedItems = subject.approved()
        then:
        approvedItems == Map.of(
                item1, productStockSnapshotOfItem1,
                item2, productStockSnapshotOfItem2,
                item3, productStockSnapshotOfItem3
        )
    }

    def "should approve cart when not all items are available"() {
        given:
        def subject = new Cart(timeProvider)
        subject.addItem(item1)
        subject.addItem(item2)
        subject.addItem(item3)
        def productSnapshotOfItem1 = new ProductStockSnapshot(1, 1)
        def productSnapshotOfItem2 = new ProductStockSnapshot(2, 0)
        def productSnapshotOfItem3 = new ProductStockSnapshot(3, 4)
        StockStateSnapshot stockState = new StockStateSnapshot(
                List.of(productSnapshotOfItem1,
                        productSnapshotOfItem2,
                        productSnapshotOfItem3))

        when:
        def isApproved = subject.approve(stockState)

        then:
        isApproved.isSuccessful()
        when:
        def approvedItems = subject.approved()
        then:
        approvedItems == Map.of(
                item1, productSnapshotOfItem1,
                item3, productSnapshotOfItem3
        )
    }

    def "should approve cart when 2 out of 4 the same items are available"() {
        given:
        def subject = new Cart(timeProvider)

        subject.addItem(item3)
        def availableItem3 = new ProductStockSnapshot(3, 2)
        StockStateSnapshot stockState = new StockStateSnapshot(
                List.of(availableItem3))
        when:
        def isApproved = subject.approve(stockState)

        then:
        isApproved.isSuccessful()
        when:
        def approvedItems = subject.approved()
        then:
        approvedItems == Map.of(item3, availableItem3)
    }


}