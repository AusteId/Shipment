package task.vinted.discount;

import task.vinted.model.Shipment;
import task.vinted.service.PriceService;

import java.math.BigDecimal;

public class SSizeLowestPriceDiscount implements DiscountRule {

  private final PriceService priceService;
  private final DiscountContext discountContext;

  public SSizeLowestPriceDiscount(PriceService priceService, DiscountContext discountContext) {
    this.priceService = priceService;
    this.discountContext = discountContext;
  }

  @Override
  public void applyDiscount(Shipment shipment) {

    if (!shipment.getPackageSize().equalsIgnoreCase("S")) {
      return;
    }

    BigDecimal lowestSPrice = priceService.getLowestPrice("S");
    BigDecimal originalPrice = shipment.getShipmentPrice();

    if (lowestSPrice == null || originalPrice.compareTo(lowestSPrice) <= 0) {
      return;
    }

    BigDecimal possibleDiscount = discountContext.getRemainingMonthlyDiscount(shipment.getShipmentDate());
    BigDecimal discount = originalPrice.subtract(lowestSPrice);
    BigDecimal actualDiscount = discount.min(possibleDiscount);

    if (actualDiscount.compareTo(BigDecimal.ZERO) > 0) {
      shipment.applyDiscount(actualDiscount);
      discountContext.addDiscount(shipment.getShipmentDate(), actualDiscount);
    }
  }
}

