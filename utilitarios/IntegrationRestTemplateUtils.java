
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Map;

public class IntegrationUtils {

    /**
     * Construtor privado
     */
    private IntegrationUtils() {
    }

    /**
     * Substitui par�metros no JSON
     *
     * @param json
     * @param mapParams
     * @return
     */
    public static String replaceParams(String json, Map<String, String> mapParams) {

        String jsonWithParams = json;
        for(String key : mapParams.keySet()) {
            if (mapParams.get(key) != null) {
                jsonWithParams = jsonWithParams.replaceAll(key, mapParams.get(key));
            }
        }

        return jsonWithParams;
    }

    /**
     * Cria objeto createHttpEntity
     *
     * @param jsonRequest
     * @return
     */
    public static HttpEntity<String> createHttpEntityForJSon(String jsonRequest, Map<String, String> mapParams) {

        // Substitui os par�metros no JSON
        if (mapParams != null) {
            jsonRequest = replaceParams(jsonRequest, mapParams);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        return new HttpEntity<String>(jsonRequest, headers);
    }

    /**
     * Cria objeto createHttpEntity
     *
     * @param jsonRequest
     * @return
     */
    public static HttpEntity<String> createHttpEntityForJSon(String jsonRequest) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        return new HttpEntity<String>(jsonRequest, headers);
    }

    /* Cria objeto createHttpEntity
     *
     * @param jsonRequest
     * @return
     */
    public static HttpEntity<String> createHttpEntityForJSon(String jsonRequest, Map<String, String> mapParams, HttpHeaders headers) {

        // Substitui os par�metros no JSON
        if (mapParams != null) {
            jsonRequest = replaceParams(jsonRequest, mapParams);
        }

        //HttpHeaders headers = new HttpHeaders();
        //headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        return new HttpEntity<String>(jsonRequest, headers);
    }
}
