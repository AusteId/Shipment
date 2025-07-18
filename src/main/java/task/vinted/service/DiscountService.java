package task.vinted.service;

import task.vinted.discount.*;
import task.vinted.model.Shipment;

import java.math.BigDecimal;
import java.util.List;

public class DiscountService {

  private final List<DiscountRule> discountRules;
  private final PriceService priceService;

  public DiscountService(List<DiscountRule> discountRules, PriceService priceService) {
    this.discountRules = discountRules;
    this.priceService = priceService;
  }

  public void applyDiscounts(Shipment shipment) {

    setShipmentPrice(shipment);

    for (DiscountRule discountRule : discountRules) {
      discountRule.applyDiscount(shipment);
    }
  }

  public void applyDiscounts(List<Shipment> shipments) {

    shipments.forEach(this::applyDiscounts);
  }

  private void setShipmentPrice(Shipment shipment) {
    BigDecimal shipmentPrice = priceService.getPriceByProviderAndSize(
            shipment.getProvider(),
            shipment.getPackageSize());

    shipment.setShipmentPrice(shipmentPrice);
  }

  public void addDiscountRule(DiscountRule discountRule) {
    discountRules.add(discountRule);
  }
}
