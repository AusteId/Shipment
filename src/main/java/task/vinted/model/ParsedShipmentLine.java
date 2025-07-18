package task.vinted.model;

public class ParsedShipmentLine {

  private Shipment shipment;
  private String originalLine;
  private boolean isValid;

  public ParsedShipmentLine(Shipment shipment, String originalLine, boolean isValid) {
    this.shipment = shipment;
    this.originalLine = originalLine;
    this.isValid = isValid;
  }

  public Shipment getShipment() {
    return shipment;
  }

  public String getOriginalLine() {
    return originalLine;
  }

  public boolean isValid() {
    return isValid;
  }
}
