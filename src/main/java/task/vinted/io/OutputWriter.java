package task.vinted.io;

import task.vinted.model.ParsedShipmentLine;

import java.util.List;

public interface OutputWriter {
  void write(List<ParsedShipmentLine> parsedShipmentLines);
}
