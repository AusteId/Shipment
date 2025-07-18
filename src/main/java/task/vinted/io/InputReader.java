package task.vinted.io;

import task.vinted.model.ParsedShipmentLine;

import java.util.List;

public interface InputReader {
  List<ParsedShipmentLine> takeShipmentsFromFile(String filePath);
}
