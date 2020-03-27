package org.sm.mw.cart;

public class ProductStockSnapshot {

    private final Integer productId;
    private final Integer reserved;

    public ProductStockSnapshot(Integer productId, Integer reserved) {
        this.productId = productId;
        this.reserved = reserved;
    }

    public Integer productId() {
        return productId;
    }

    public Integer reserved() {
        return reserved;
    }
}


