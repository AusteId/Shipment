package task.vinted.discount;

import task.vinted.model.Shipment;

public interface DiscountRule {
  void applyDiscount(Shipment shipment);
}
