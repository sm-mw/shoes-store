package org.sm.mw.cart;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Cart {

    List<CartItem> items = new ArrayList<>();
    Status status = Status.ACTIVE;
    Instant lastModification;
    Clock clock;

    enum Status {ACTIVE, ABANDONED}

    Cart(Clock clock) {
        this.clock = clock;
        this.lastModification = clock.instant();
    }

    Result addItem(CartItem item) {
        items.add(item);
        status = Status.ACTIVE;
        return Result.success();
    }

    Result removeItem(CartItem item) {
        if (items.isEmpty()) {
            return Result.failure();
        }
        items.remove(item);
        return Result.success();
    }

    boolean isAbandoned() {
        return isAbandoned(clock.instant());
    }

    boolean isAbandoned(Instant afterTime) {
        if (afterTime.isAfter(lastModification.plus(2, ChronoUnit.DAYS))) {
            status = Status.ABANDONED;
        }
        return Status.ABANDONED == status;
    }

    Result applyPromoCode() {
        return Result.failure();
    }

    static class Result {

        private boolean successfull;

        private Result(boolean isSuccess) {
            this.successfull = isSuccess;
        }

        boolean isSuccessful() {
            return this.successfull;
        }

        static Result success() {
            return new Result(true);
        }

        static Result failure() {
            return new Result(false);
        }

    }
}
