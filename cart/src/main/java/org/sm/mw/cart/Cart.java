package org.sm.mw.cart;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Cart {

    private List<CartItem> items = new ArrayList<>();
    private Status status = Status.ACTIVE;
    private TimeProvider timeProvider;
    private Instant lastModified;

    enum Status {ACTIVE, ABANDONED}

    public Cart(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
        lastModified = timeProvider.now();

    }

    public Instant whenLastModified() {
        return lastModified;
    }

    Result addItem(CartItem item) {
        items.add(item);
        markCartAsActive();
        updateLastModified();
        return Result.success();
    }


    Result removeItem(CartItem item) {
        if (items.isEmpty()) {
            return Result.failure();
        }
        items.remove(item);
        markCartAsActive();
        updateLastModified();
        return Result.success();
    }

    boolean isAbandoned() {
        if (timeProvider.now().isAfter(lastModified.plus(2, ChronoUnit.DAYS))) {
            status = Status.ABANDONED;
        }
        return Status.ABANDONED == status;
    }

    Result markAsAbandoned() {
        return Result.failure();
    }

    Result applyPromoCode() {
        return Result.failure();
    }

    private void updateLastModified() {
        this.lastModified = timeProvider.now();
    }

    private void markCartAsActive() {
        status = Status.ACTIVE;
    }

    static class Result {

        private boolean successful;

        private Result(boolean isSuccess) {
            this.successful = isSuccess;
        }

        boolean isSuccessful() {
            return this.successful;
        }

        static Result success() {
            return new Result(true);
        }

        static Result failure() {
            return new Result(false);
        }

    }
}
