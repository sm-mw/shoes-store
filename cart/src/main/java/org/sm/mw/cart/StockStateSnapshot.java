package org.sm.mw.cart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StockStateSnapshot {

    private final Map<Integer, ProductStockSnapshot> availableProducts;

    public StockStateSnapshot(List<ProductStockSnapshot> availableProducts) {
        this.availableProducts = availableProducts.stream()
                .collect(Collectors.toMap(ProductStockSnapshot::productId, Function.identity()));
    }


    public Map<CartItem, ProductStockSnapshot> availableItems(List<CartItem> items) {
        return items.stream().filter(this::isAvailable)
                .collect(Collectors.toMap(Function.identity(), item -> availableProducts.get(item.productId())));
    }

    private boolean isAvailable(CartItem item) {
        return availableProducts.containsKey(item.productId())
                && availableProducts.get(item.productId()).reserved() > 0;
    }
}
