package task.vinted;

import task.vinted.discount.*;
import task.vinted.io.ConsoleOutputWriter;
import task.vinted.io.FileInputReader;
import task.vinted.io.OutputWriter;
import task.vinted.io.InputReader;
import task.vinted.model.ParsedShipmentLine;
import task.vinted.service.ConfigService;
import task.vinted.service.DiscountService;
import task.vinted.service.PriceService;
import task.vinted.service.ShipmentParser;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class Main {

  private static final String FILE_PATH = "src/main/resources/input.txt";

  public static void main(String[] args) throws IOException {

    PriceService priceService = new PriceService();

    ConfigService configService = new ConfigService();
    ShipmentParser shipmentParser = new ShipmentParser(configService);
    InputReader reader = new FileInputReader(shipmentParser);

    String filePath = args.length > 0 && args[0] != null && !args[0].isBlank() ? args[0] : FILE_PATH;

    List<ParsedShipmentLine> shipments = reader.takeShipmentsFromFile(filePath);
    DiscountContext discountContext = new DiscountContext(BigDecimal.TEN);

    List<DiscountRule> discountRules = List.of(
            new SSizeLowestPriceDiscount(priceService, discountContext),
            new LSizeLPThirdFreeDiscount(priceService, discountContext)
    );

    DiscountService discountService = new DiscountService(discountRules, priceService);

    shipments.stream().filter(ParsedShipmentLine::isValid).forEach(info -> discountService.applyDiscounts(info.getShipment()));

    OutputWriter outputWriter = new ConsoleOutputWriter();
    outputWriter.write(shipments);
  }
}