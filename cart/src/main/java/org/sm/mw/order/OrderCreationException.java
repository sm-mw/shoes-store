package org.sm.mw.order;

public class OrderCreationException extends RuntimeException {

    private static final String MESSAGE = "Items can not be empty or null.";

    public OrderCreationException() {
        super(MESSAGE);
    }

    public String message() {
        return MESSAGE;
    }
}
