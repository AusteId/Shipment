package task.vinted.service;

import java.io.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class PriceService {

  private static final String PRICE_FILE = "src/main/resources/price.csv";
  private final Map<String, BigDecimal> dataFromFile;

  public PriceService() {
    try {
      this.dataFromFile = takeDataFromFile(PRICE_FILE);
    } catch (IOException e) {
      throw new UncheckedIOException("Failed to load price data", e);
    }
  }

  public Map<String, BigDecimal> takeDataFromFile(String filePath) throws IOException {

    Map<String, BigDecimal> dataFromFile = new HashMap<>();

    try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath))) {

      String line;
      while ((line = fileReader.readLine()) != null) {
        String[] lineItems = line.split(",");
        String providerAndSize = lineItems[0].concat("-" + lineItems[1]);
        dataFromFile.put(providerAndSize, new BigDecimal(lineItems[2]));
      }
    }
    return dataFromFile;
  }

  public BigDecimal getPriceByProviderAndSize(String provider, String size) {
    return dataFromFile.get(provider + "-" + size);
  }

  public BigDecimal getLowestPrice(String size) {

    BigDecimal lowestPrice = null;
    String lowerSize = size.toLowerCase();

    for (Map.Entry<String, BigDecimal> entry : dataFromFile.entrySet()) {
      if (entry.getKey().toLowerCase().contains(lowerSize)) {
        BigDecimal price = entry.getValue();
        if (lowestPrice == null || price.compareTo(lowestPrice) < 0) {
          lowestPrice = price;
        }
      }
    }
    return lowestPrice;
  }

  public BigDecimal getLSizeLPProviderPrice(String provider, String size) {
    return dataFromFile.get(provider + "-" + size);
  }
}

