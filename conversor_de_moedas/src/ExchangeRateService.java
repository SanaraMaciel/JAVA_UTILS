import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Map;

public class ExchangeRateService {
    private final String apiKey;

    public ExchangeRateService(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getRates() throws Exception {
        String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/USD";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
       // System.out.println("JSON Response: " + response.body());
        return response.body();
    }

    public Map<String, Double> parseRates(String jsonResponse) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);

        if (jsonObject.has("result") && jsonObject.get("result").getAsString().equals("success")) {
            if (jsonObject.has("conversion_rates")) {
                JsonObject ratesObject = jsonObject.getAsJsonObject("conversion_rates");
                Type type = new TypeToken<Map<String, Double>>() {}.getType();
                return gson.fromJson(ratesObject, type);
            } else {
                System.out.println("Failed to parse rates: 'conversion_rates' not found in response");
                return null;
            }
        } else {
            System.out.println("Error in response: " + jsonResponse);
            return null;
        }
    }
}
