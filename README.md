# Shipping Discount App

This is a simple Java application that processes a list of shipments, applies discount rules based on size and provider, and prints the final result.

## Requirements

- Java 21
- Maven 3.x

## Build the Project

To build the project and generate the `.jar` file, run the following command from the root directory:

```bash
mvn clean package
```

After building, the output JAR file will be located at:

```
target/vinted-app.jar
```

## Run the Application

To execute the program with your input file:

```bash
java -jar target/vinted-app.jar "src/main/resources/input.txt"
```

> Make sure the input file path is correct and properly formatted.

## Run Tests

To run unit tests:

```bash
mvn test
```

## Notes

- The application expects one shipment per line in the input file.
- Invalid or malformed lines are ignored with an appropriate message.
- The output preserves the order of the input and displays discounts where applicable.
