package org.sm.mw.cart;

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

    public int amountAvailable(CartItem item) {
        return availableProducts.getOrDefault(item.productId(), new ProductStockSnapshot(item.productId(), 0)).reserved();
    }
}
