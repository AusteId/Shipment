package task.vinted.discount;

import task.vinted.model.Shipment;
import task.vinted.service.PriceService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

public class LSizeLPThirdFreeDiscount implements DiscountRule {

  private PriceService priceService;
  private DiscountContext discountContext;

  public LSizeLPThirdFreeDiscount(PriceService priceService, DiscountContext discountContext) {
    this.priceService = priceService;
    this.discountContext = discountContext;
  }

  @Override
  public void applyDiscount(Shipment shipment) {

    if (!shipment.getProvider().equalsIgnoreCase("LP") ||
            !shipment.getPackageSize().equalsIgnoreCase("L")) {
      return;
    }

    LocalDate date = shipment.getShipmentDate();
    YearMonth yearMonth = YearMonth.from(date);
    int currentCount = discountContext.getLSizeLPCountForMonth(yearMonth);
    discountContext.incrementLSizeLPCount(yearMonth);

    if (currentCount == 2) {
      BigDecimal discount = priceService.getLSizeLPProviderPrice("LP", "L");
      BigDecimal remaining = discountContext.getRemainingMonthlyDiscount(date);

      if (remaining.compareTo(BigDecimal.ZERO) > 0) {
        BigDecimal appliedDiscount = discountContext.addDiscount(date, discount);
        shipment.applyDiscount(appliedDiscount);
      }
    }
  }
}