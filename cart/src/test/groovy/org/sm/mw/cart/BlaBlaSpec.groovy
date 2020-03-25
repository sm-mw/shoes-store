package org.sm.mw.cart

import spock.lang.Specification

class BlaBlaSpec extends Specification {

    def "should add new item to cart"() {
        given:
        CartItem item = new CartItem()

        expect:
        new BlaBla().addItem(item).isSuccessful()
    }

    def "should remove added item from cart"() {
        given:
        def subject = new BlaBla()
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
        def subject = new BlaBla()
        def item = new CartItem()
        when:
        def result = subject.removeItem(item)

        then:
        !result.isSuccessful()
    }
    
    def "should mark cart as abandoned"() {
        given:
        def subject = new BlaBla()

        when:
        def result = subject.markAsAbandoned()

        then:
        result.isSuccessful()
    }
    
    def "should apply promo code when provided"() {
        given:
        def subject = new BlaBla()
        
        when:
        def result = subject.applyPromoCode()
        
        then:
        result.isSuccessful();
    }
}  