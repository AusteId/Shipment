package task.vinted.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

public class ConfigService {

  private static final String CONFIG_PATH = "src/main/resources/config.properties";
  private final Set<String> validSizes;
  private final Set<String> validProviders;

  public ConfigService() throws IOException {

    Properties properties = new Properties();

    try (FileInputStream configInput = new FileInputStream(CONFIG_PATH)) {
      properties.load(configInput);
    }

    this.validSizes = Set.of(properties.getProperty("valid.sizes").split(","));
    this.validProviders = Set.of(properties.getProperty("valid.providers").split(","));
  }

  public boolean isValidSize(String size) {
    return validSizes.contains(size);
  }

  public boolean isValidProvider(String provider) {
    return validProviders.contains(provider);
  }
}
