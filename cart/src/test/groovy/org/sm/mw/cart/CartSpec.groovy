package org.sm.mw.cart

import spock.lang.Specification

import java.time.Instant
import java.time.temporal.ChronoUnit

class CartSpec extends Specification {

    TimeProvider timeProvider = Mock(TimeProvider)
    def fixedTime = Instant.parse("2020-03-26T18:35:24.00Z")

    def setup() {
        timeProvider.now() >> fixedTime
    }

    def "should add new item to cart"() {
        given:
        CartItem item = new CartItem()

        expect:
        new Cart(timeProvider).addItem(item).isSuccessful()
    }

    def "should add new item to cart if available in warehouse"() {
        given:
        CartItem item = new CartItem()


        expect:
        new Cart(timeProvider).addItem(item).isSuccessful()

    }

    def "should remove added item from cart"() {
        given:
        def subject = new Cart(timeProvider)
        def item = new CartItem()

        when:
        subject.addItem(item)

        then:
        subject.removeItem(item).isSuccessful()
        and:
        !subject.removeItem(item).isSuccessful()
    }

    def "should not remove item when it is not present in cart"() {
        given:
        def subject = new Cart(timeProvider)
        def item = new CartItem()
        when:
        def result = subject.removeItem(item)

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
        def cartItem = new CartItem()
        subject.addItem(cartItem)

        when:
        subject.removeItem(cartItem)

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
        result.successful()
    }
}  