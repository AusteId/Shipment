package task.vinted.service;

import task.vinted.model.ParsedShipmentLine;

import java.time.LocalDate;
import java.util.List;

public class DataOrderValidator {

  public boolean isDateOrderValid(List<ParsedShipmentLine> lines) {

    LocalDate previous = null;

    for (ParsedShipmentLine line : lines) {
      if (!line.isValid()) {
        continue;
      }

      LocalDate current = line.getShipment().getShipmentDate();

      if (previous != null && current.isBefore(previous)) {
        return false;
      }

      previous = current;
    }
    return true;
  }
}
