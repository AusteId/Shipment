package task.vinted.io;

import task.vinted.model.ParsedShipmentLine;

import java.util.List;

public class ConsoleOutputWriter implements OutputWriter {

  @Override
  public void write(List<ParsedShipmentLine> parsedShipmentLines) {
    for (ParsedShipmentLine info : parsedShipmentLines) {
      if (!info.isValid()) {
        System.out.println(info.getOriginalLine() + " Ignored");
      } else {
        System.out.println(info.getShipment());
      }
    }
  }
}
