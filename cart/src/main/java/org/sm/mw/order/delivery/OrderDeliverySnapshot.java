package org.sm.mw.order.delivery;

import java.util.Arrays;
import java.util.List;

public class OrderDeliverySnapshot {

  private DeliveryProvider deliveryProvider;

  public static OrderDeliverySnapshot create() {
    return null;
  }

  public List<DeliveryProvider> availableOptions() {
    return Arrays.asList(DeliveryProvider.POST_OFFICE, DeliveryProvider.COURIER);
  }

}
