package org.sm.mw.cart;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.sm.mw.cart.discount.DiscountPolicy;
import org.sm.mw.cart.discount.NoDiscountPolicy;
import org.sm.mw.cart.snapshot.CartSnapshot;

public class Cart {

    private List<CartItem> items = new ArrayList<>();
    private TimeProvider timeProvider;
    private Instant lastModified;
    private boolean approved;
    private List<ApprovedItemSnapshot> approvedItemSnapshots = Collections.emptyList();
    private DiscountPolicy discountPolicy = new NoDiscountPolicy();

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

    Result applyDiscount(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
        return Result.success();
    }

    Result approve(StockStateSnapshot stockState) {
        if (items.isEmpty()) {
            return Result.failure();
        }
        this.approved = true;

        this.approvedItemSnapshots = this.items.stream()
            .filter(item -> stockState.amountAvailable(item) > 0)
            .map(item -> new ApprovedItemSnapshot(item.snapshot(discountPolicy.discount(item)), stockState.amountAvailable(item)))
            .collect(Collectors.toList());

        return Result.success();
    }

    List<ApprovedItemSnapshot> approved() {
        return approvedItemSnapshots;
    }

    private void updateLastModified() {
        this.lastModified = timeProvider.now();
    }

    public CartSnapshot cartSummary() {
        return new CartSnapshot(this.items.stream()
            .map(cartItem ->
                cartItem.snapshot(discountPolicy.discount(cartItem)))
            .collect(Collectors.toList()));
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
