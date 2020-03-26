package org.sm.mw.cart;

import java.time.Instant;

public interface TimeProvider {
    Instant now();
}
