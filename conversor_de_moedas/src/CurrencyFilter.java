import java.util.HashMap;
import java.util.Map;

public class CurrencyFilter {
    public static Map<String, Double> filterCurrencies(Map<String, Double> rates) {
        Map<String, Double> filteredRates = new HashMap<>();
        String[] selectedCurrencies = {"USD", "EUR", "GBP", "JPY", "AUD", "CAD","BRL"};

        for (String currency : selectedCurrencies) {
            if (rates.containsKey(currency)) {
                filteredRates.put(currency, rates.get(currency));
            }
        }
        return filteredRates;
    }
}
