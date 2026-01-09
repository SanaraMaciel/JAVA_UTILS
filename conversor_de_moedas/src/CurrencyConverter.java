import java.sql.*;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CurrencyConverter {
    private static final Logger logger = Logger.getLogger(CurrencyConverter.class.getName());

    public static void main(String[] args) {
        try {
            System.out.println("Currency Converter is running...");
            ExchangeRateService exchangeRateService = new ExchangeRateService("9931099cc4b0bf1b7e2389bf");
            String jsonResponse = exchangeRateService.getRates();
            Map<String, Double> rates = exchangeRateService.parseRates(jsonResponse);
            if (rates == null) {
                System.out.println("Failed to retrieve exchange rates. Exiting...");
                return;
            }
            Map<String, Double> filteredRates = CurrencyFilter.filterCurrencies(rates);

            Scanner scanner = new Scanner(System.in);
            System.out.println("Escolha a moeda de origem:");
            int index = 1;
            for (String currency : filteredRates.keySet()) {
                System.out.println(index + ". " + currency);
                index++;
            }

            int fromIndex = scanner.nextInt();
            String fromCurrency = filteredRates.keySet().toArray(new String[0])[fromIndex - 1];

            System.out.println("Escolha a moeda de destino:");
            index = 1;
            for (String currency : filteredRates.keySet()) {
                System.out.println(index + ". " + currency);
                index++;
            }

            int toIndex = scanner.nextInt();
            String toCurrency = filteredRates.keySet().toArray(new String[0])[toIndex - 1];

            System.out.println("Digite o valor a ser convertido:");
            double amount = scanner.nextDouble();

            double rate = filteredRates.get(toCurrency) / filteredRates.get(fromCurrency);
            double convertedAmount = amount * rate;

            System.out.println(amount + " " + fromCurrency + " = " + convertedAmount + " " + toCurrency);

            // Registrar a conversão no banco de dados
            saveConversionToDatabase(fromCurrency, toCurrency, amount, convertedAmount, rate);

            // Mostrar histórico de conversões
            showConversionHistory();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error occurred", e);
        }
    }

    private static void saveConversionToDatabase(String fromCurrency, String toCurrency, double amount, double convertedAmount, double rate) {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String sql = "INSERT INTO conversion_history (from_currency, to_currency, amount, converted_amount, conversion_rate) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, fromCurrency);
            statement.setString(2, toCurrency);
            statement.setDouble(3, amount);
            statement.setDouble(4, convertedAmount);
            statement.setDouble(5, rate);
            statement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(CurrencyConverter.class.getName()).log(Level.SEVERE, "Database error", e);
        }
    }

    private static void showConversionHistory() {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String sql = "SELECT * FROM conversion_history ORDER BY conversion_date DESC";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            System.out.println("Histórico de Conversões:");
            while (resultSet.next()) {
                String fromCurrency = resultSet.getString("from_currency");
                String toCurrency = resultSet.getString("to_currency");
                double amount = resultSet.getDouble("amount");
                double convertedAmount = resultSet.getDouble("converted_amount");
                double conversionRate = resultSet.getDouble("conversion_rate");
                Timestamp conversionDate = resultSet.getTimestamp("conversion_date");

                System.out.printf("%s: %f %s -> %f %s (Taxa: %f)%n",
                        conversionDate, amount, fromCurrency, convertedAmount, toCurrency, conversionRate);
            }
        } catch (SQLException e) {
            Logger.getLogger(CurrencyConverter.class.getName()).log(Level.SEVERE, "Database error", e);
        }
    }

}
