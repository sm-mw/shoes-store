package org.sm.mw.cart;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cart {

    private List<CartItem> items = new ArrayList<>();
    private TimeProvider timeProvider;
    private Instant lastModified;
    private boolean approved;
    private List<ApprovedItem> approvedItems = Collections.emptyList();

    public Cart(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
        lastModified = timeProvider.now();

    }

    Result addItem(CartItem item) {
        if (approved) {
            return Result.failure();
        }
        items.add(item);
        updateLastModified();
        return Result.success();
    }


    Result removeItem(CartItem item) {
        if (items.isEmpty() || approved || !items.contains(item)) {
            return Result.failure();
        }
        items.remove(item);
        updateLastModified();
        return Result.success();
    }

    boolean isAbandoned() {
        return timeProvider.now().isAfter(lastModified.plus(2, ChronoUnit.DAYS));
    }

    Result applyPromoCode() {
        return Result.failure();
    }


    Result approve(StockStateSnapshot stockState) {
        if (items.isEmpty()) {
            return Result.failure();
        }
        this.approved = true;
        this.approvedItems = stockState.availableItems(this.items);
        return Result.success();
    }

    List<ApprovedItem> approved() {
        return approvedItems;
    }


    private void updateLastModified() {
        this.lastModified = timeProvider.now();
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
