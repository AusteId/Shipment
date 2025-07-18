package task.vinted.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import task.vinted.model.ParsedShipmentLine;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShipmentParserTest {

  @Mock
  private ConfigService configService;

  @InjectMocks
  private ShipmentParser shipmentParser;

  @Test
  void shouldParseValidLine() {

    when(configService.isValidSize("S")).thenReturn(true);
    when(configService.isValidProvider("MR")).thenReturn(true);
    String input = "2015-02-01 S MR";

    ParsedShipmentLine result = shipmentParser.parseLine(input);

    assertTrue(result.isValid(), "Shipment line should be valid");
    assertNotNull(result.getShipment(), "Shipment should not be null");
    assertEquals(LocalDate.of(2015, 2, 1), result.getShipment().getShipmentDate(), "Date should match");
    assertEquals("S", result.getShipment().getPackageSize(), "Size should match");
    assertEquals("MR", result.getShipment().getProvider(), "Provider should match");
    assertEquals(input, result.getOriginalLine(), "Original line should match");
  }

  @Test
  void shouldRejectInvalidSize() {

    when(configService.isValidSize("X")).thenReturn(false);
    String input = "2015-02-01 X MR";

    ParsedShipmentLine result = shipmentParser.parseLine(input);

    assertFalse(result.isValid(), "Shipment line should be invalid");
    assertNull(result.getShipment(), "Shipment should be null");
    assertEquals(input, result.getOriginalLine(), "Original line should match");
  }

  @Test
  void shouldRejectInvalidFormat() {

    String input = "2015-02-01 S";

    ParsedShipmentLine result = shipmentParser.parseLine(input);

    assertFalse(result.isValid(), "Shipment line should be invalid");
    assertNull(result.getShipment(), "Shipment should be null");
    assertEquals(input, result.getOriginalLine(), "Original line should match");
  }
}
