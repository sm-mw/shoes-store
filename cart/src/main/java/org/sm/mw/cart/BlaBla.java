package org.sm.mw.cart;

import java.util.ArrayList;
import java.util.List;

public class BlaBla {

    List<CartItem> items = new ArrayList<>();

    Result addItem(CartItem item) {
        items.add(item);
        return Result.success();
    }

    Result removeItem(CartItem item) {
        if (items.isEmpty()) {
            return Result.failure();
        }
        items.remove(item);
        return Result.success();
    }
    
    Result markAsAbandoned() {
        return Result.failure();
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
