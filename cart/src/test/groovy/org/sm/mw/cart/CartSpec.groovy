package org.sm.mw.cart

import org.sm.mw.cart.discount.DiscountPolicy
import org.sm.mw.cart.discount.WholeCartDiscountPolicy
import org.sm.mw.cart.snapshot.CartItemSnapshot
import spock.lang.Specification
import spock.lang.Unroll

import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.stream.Collectors

class CartSpec extends Specification {

    TimeProvider timeProvider = Mock(TimeProvider)
    def fixedTime = Instant.parse("2020-03-26T18:35:24.00Z")
    def static item1 = new CartItem(1, 1, new BigDecimal("123.99"))
    def static item2 = new CartItem(2, 3, new BigDecimal("123.99"))
    def static item3 = new CartItem(3, 4, new BigDecimal("123.99"))
    def static item4 = new CartItem(4, 1, new BigDecimal("123.99"))


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
        subject.addItem(new CartItem(1, 1, new BigDecimal("100.00")))
        subject.addItem(new CartItem(2, 1, new BigDecimal("200.00")))

        DiscountPolicy p = new WholeCartDiscountPolicy(new BigDecimal("20.0"))

        when:
        def result = subject.applyDiscount(p)

        then:
        result.isSuccessful()

        when:
        def cartSnapshot = subject.cartSummary()

        then:
        cartSnapshot.items() as Set == [new CartItemSnapshot(1, 1, new BigDecimal("100.00"), new BigDecimal("80.00")),
                                        new CartItemSnapshot(2, 1, new BigDecimal("200.00"), new BigDecimal("160.00"))] as Set
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
                        [
                                new ProductStockSnapshot(1, 1),
                                new ProductStockSnapshot(2, 3),
                                new ProductStockSnapshot(3, 4)
                        ]
                ),
                new StockStateSnapshot(
                        [
                                new ProductStockSnapshot(1, 1),
                                new ProductStockSnapshot(2, 0),
                                new ProductStockSnapshot(3, 4)
                        ]
                ),
                new StockStateSnapshot(
                        [
                                new ProductStockSnapshot(1, 1),
                                new ProductStockSnapshot(2, 3),
                                new ProductStockSnapshot(3, 2)
                        ]
                )
        ]
        expectedItems << [
                [
                        new ApprovedItemSnapshot(item1.snapshot(new BigDecimal("123.99")), 1),
                        new ApprovedItemSnapshot(item2.snapshot(new BigDecimal("123.99")), 3),
                        new ApprovedItemSnapshot(item3.snapshot(new BigDecimal("123.99")), 4)
                ],
                [
                        new ApprovedItemSnapshot(item1.snapshot(new BigDecimal("123.99")), 1),
                        new ApprovedItemSnapshot(item3.snapshot(new BigDecimal("123.99")), 4)
                ],
                [
                        new ApprovedItemSnapshot(item1.snapshot(new BigDecimal("123.99")), 1),
                        new ApprovedItemSnapshot(item2.snapshot(new BigDecimal("123.99")), 3),
                        new ApprovedItemSnapshot(item3.snapshot(new BigDecimal("123.99")), 2)
                ],
        ]
    }

    def "should apply discount when cart is approved "() {
        given:
        def subject = new Cart(timeProvider)
        subject.addItem(new CartItem(1, 1, new BigDecimal("100.00")))
        subject.addItem(new CartItem(2, 1, new BigDecimal("200.00")))

        DiscountPolicy p = new WholeCartDiscountPolicy(new BigDecimal("20.0"))
        subject.applyDiscount(p)

        def stockState = new StockStateSnapshot(
                [
                        new ProductStockSnapshot(1, 1),
                        new ProductStockSnapshot(2, 3),
                        new ProductStockSnapshot(3, 4)
                ]
        )

        when:
        def result = subject.approve(stockState)

        then:
        result.isSuccessful()

        when:
        def approvedCartItems = subject.approved()
                .stream()
                .map(approvedItem -> approvedItem.cartItem())
                .collect(Collectors.toList())

        then:
        approvedCartItems as Set == [new CartItemSnapshot(1, 1, new BigDecimal("100.00"), new BigDecimal("80.00")),
                                     new CartItemSnapshot(2, 1, new BigDecimal("200.00"), new BigDecimal("160.00"))] as Set


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
