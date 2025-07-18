package task.vinted.service;

import task.vinted.model.Shipment;
import task.vinted.model.ParsedShipmentLine;

import java.time.LocalDate;

public class ShipmentParser {

  private final ConfigService configService;

  public ShipmentParser(ConfigService configService) {
    this.configService = configService;
  }

  public ParsedShipmentLine parseLine(String line) {
    try {
      String[] items = line.split(" ");

      if (items.length != 3) {
        return new ParsedShipmentLine(null, line, false);
      }

      LocalDate date = LocalDate.parse(items[0]);
      String size = items[1];
      String provider = items[2];

      if (configService.isValidSize(size) && configService.isValidProvider(provider)) {
        Shipment shipment = new Shipment(date, size, provider);
        return new ParsedShipmentLine(shipment, line, true);
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return new ParsedShipmentLine(null, line, false);
    }

    return new ParsedShipmentLine(null, line, false);
  }
}

