package task.vinted.io;

import task.vinted.model.ParsedShipmentLine;
import task.vinted.service.DataOrderValidator;
import task.vinted.service.ShipmentParser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileInputReader implements InputReader {

  private final ShipmentParser shipmentParser;

  public FileInputReader(ShipmentParser shipmentParser) {
    this.shipmentParser = shipmentParser;
  }

  public List<ParsedShipmentLine> takeShipmentsFromFile(String filePath) {

    List<ParsedShipmentLine> shipments = new ArrayList<>();

    try (
            BufferedReader fileReader = new BufferedReader(new FileReader(filePath));
    ) {

      String line;
      while ((line = fileReader.readLine()) != null) {
        ParsedShipmentLine parsedShipmentLine = shipmentParser.parseLine(line);
        shipments.add(parsedShipmentLine);
      }
    } catch (IOException e) {
      throw new UncheckedIOException("Failed to read file: " + filePath, e);
    }

    DataOrderValidator validator = new DataOrderValidator();
    if (!validator.isDateOrderValid(shipments)) {
      System.out.println("WARNING: input dates are not in chronological order");
    }
    return shipments;
  }
}