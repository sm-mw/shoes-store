package org.sm.mw.cart

import spock.lang.Specification
import spock.lang.Unroll

import java.time.Instant
import java.time.temporal.ChronoUnit

class CartSpec extends Specification {

    TimeProvider timeProvider = Mock(TimeProvider)
    def fixedTime = Instant.parse("2020-03-26T18:35:24.00Z")
    def static item1 = new CartItem(1, 1)
    def static item2 = new CartItem(2, 3)
    def static item3 = new CartItem(3, 4)
    def static item4 = new CartItem(4, 1)


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
        subject.addItem(item2)
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

    @Unroll
    def "should approve cart"() {
        given:
        def subject = new Cart(timeProvider)
        subject.addItem(item1)
        subject.addItem(item2)
        subject.addItem(item3)
        when:
        def isApproved = subject.approve(stockStateSnapshot)
        then:
        isApproved.isSuccessful()
        !subject.addItem(item4).isSuccessful()
        !subject.removeItem(item4).isSuccessful()
        when:
        def approvedItems = subject.approved()
        then:
        approvedItems == expectedItems

        where:
        stockStateSnapshot << [
                new StockStateSnapshot(
                        [new ProductStockSnapshot(1, 1),
                         new ProductStockSnapshot(2, 3),
                         new ProductStockSnapshot(3, 4)
                        ]
                ),
                new StockStateSnapshot(
                    [new ProductStockSnapshot(1, 1),
                     new ProductStockSnapshot(2, 0),
                     new ProductStockSnapshot(3, 4)
                    ]
                ),
                new StockStateSnapshot(
                        [new ProductStockSnapshot(1, 1),
                         new ProductStockSnapshot(2, 3),
                         new ProductStockSnapshot(3, 2)
                        ]
                )
        ]
         expectedItems << [
                 List.of(
                         new ApprovedItem(item1, 1),
                         new ApprovedItem(item2, 3),
                         new ApprovedItem(item3, 4)
                 ),
                 List.of(
                         new ApprovedItem(item1, 1),
                         new ApprovedItem(item3, 4)
                 ),
                 List.of(
                         new ApprovedItem(item1, 1),
                         new ApprovedItem(item2, 3),
                         new ApprovedItem(item3, 2)
                 ),
         ]
    }

    def "should not approve when empty cart"() {
        given:
        def subject = new Cart(timeProvider)

        when:
        def approved = subject.approve(new StockStateSnapshot(Collections.emptyList()))

        then:
        !approved.isSuccessful()
    }
}