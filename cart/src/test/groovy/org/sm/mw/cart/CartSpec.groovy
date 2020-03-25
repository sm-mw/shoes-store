package org.sm.mw.cart

import spock.lang.Specification

import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class CartSpec extends Specification {

    Clock fixed = Clock.fixed(Instant.now(), ZoneId.systemDefault())

    def "should add new item to cart"() {
        given:
        CartItem item = new CartItem()

        expect:
        new Cart(fixed).addItem(item).isSuccessful()
    }

    def "should remove added item from cart"() {
        given:
        def subject = new Cart(fixed)
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
        def subject = new Cart(fixed)
        def item = new CartItem()
        when:
        def result = subject.removeItem(item)

        then:
        !result.isSuccessful()
    }

    def "should check if cart is not abandoned"() {
        given:
        def subject = new Cart(fixed)

        when:
        def isAbandoned = subject.isAbandoned(Instant.from(fixed.instant().plus(1, ChronoUnit.DAYS)))

        then:
        !isAbandoned
    }

    def "should check if cart is abandoned after 3 days"() {
        Clock fixed = Clock.fixed(Instant.now(), ZoneId.systemDefault())

        given:
        def subject = new Cart(fixed)

        when:
        def isAbandoned = subject.isAbandoned(Instant.from(fixed.instant().plus(3, ChronoUnit.DAYS)))

        then:
        isAbandoned
    }

    def "should activate abandoned cart when item added"() {
        Clock fixed = Clock.fixed(Instant.now(), ZoneId.systemDefault())

        given:
        def subject = new Cart(fixed)
        subject.isAbandoned(Instant.from(fixed.instant().plus(3, ChronoUnit.DAYS)))

        when:
        subject.addItem()
        def isAbandoned = subject.isAbandoned(Instant.from(fixed.instant().plus(1, ChronoUnit.DAYS)))

        then:
        !isAbandoned
    }

    def "should apply promo code when provided"() {
        given:
        def subject = new Cart(fixed)

        when:
        def result = subject.applyPromoCode()

        then:
        result.isSuccessful()
    }
}  