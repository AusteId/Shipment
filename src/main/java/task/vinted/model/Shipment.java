package task.vinted.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class Shipment {

  private LocalDate shipmentDate;
  private String packageSize;
  private String provider;
  private BigDecimal shipmentPrice;
  private BigDecimal discountAmount;

  public Shipment(LocalDate shipmentDate, String packageSize, String provider,
                  BigDecimal shipmentPrice, BigDecimal discountAmount) {
    this.shipmentDate = shipmentDate;
    this.packageSize = packageSize;
    this.provider = provider;
    this.shipmentPrice = shipmentPrice;
    this.discountAmount = discountAmount;
  }

  public Shipment(LocalDate shipmentDate, String packageSize, String provider) {
    this.shipmentDate = shipmentDate;
    this.packageSize = packageSize;
    this.provider = provider;
    this.shipmentPrice = BigDecimal.ZERO;
    this.discountAmount = BigDecimal.ZERO;
  }

  public LocalDate getShipmentDate() {
    return shipmentDate;
  }

  public String getPackageSize() {
    return packageSize;
  }

  public String getProvider() {
    return provider;
  }

  public BigDecimal getShipmentPrice() {
    return shipmentPrice;
  }

  public BigDecimal getDiscountAmount() {
    return discountAmount;
  }

  public void setShipmentPrice(BigDecimal shipmentPrice) {
    this.shipmentPrice = shipmentPrice.setScale(2, RoundingMode.HALF_UP);
  }

  public void setDiscountAmount(BigDecimal discountAmount) {
    this.discountAmount = discountAmount.setScale(2, RoundingMode.HALF_UP);
  }

  public void applyDiscount(BigDecimal discount) {
    setDiscountAmount(discount);
    setShipmentPrice(shipmentPrice.subtract(discount));
  }

  @Override
  public String toString() {
    return shipmentDate + " " + packageSize + " " + provider + " " +
            shipmentPrice + " " +
            ((discountAmount.compareTo(BigDecimal.ZERO) == 0) ? "-" : discountAmount);
  }
}
