
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.SocketTimeoutException;
import java.util.*;

@Controller
public class ManagerIntegrationEVCS {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManagerIntegrationEVCS.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ConfigurationData configurationData;


    /**
     * Busca dados dos eletroposto -- listCpo2Ders
     *
     * @return dados do eletroposto
     */
    public GetChargePointRes getListCPO2Ders() throws ApiException {

        // Json de request
        String jsonRequest = "{}";

        // Parâmetros de request
        Map<String, String> mapParams = new HashMap<>();

        GetChargePointRes getChargePointRes = null;

        try {
            String url = configurationData.getUrlIntegrationManager("listCpo2Ders");
            HttpEntity<String> httpEntity = IntegrationUtils.createHttpEntityForJSon(jsonRequest, mapParams);
            LOGGER.info("[Integration] Call " + url + " --> " + httpEntity.getBody());

            // Faz a chamada do serviço de integração
            getChargePointRes = restTemplate.postForObject(url, httpEntity, GetChargePointRes.class);

            //faz verificação de qual eletroposto eu posso retornar removendo da
            // lista os que já estão sendo usados pelo evcs e v2g
            List<ChargePointData> chargePointTotal = getChargePointRes.getChargePointKey();
            List<ChargePointData> chargePointsNotUtilized = verifyEpToUse(chargePointTotal, jsonRequest, mapParams);
            getChargePointRes.setChargePointKey(chargePointsNotUtilized);

            return getChargePointRes;

        } catch (Throwable e) {
            onError(e);
            return null;
        }
    }

    private List<ChargePointData> verifyEpToUse(List<ChargePointData> chargePointTotal, String jsonRequest, Map<String, String> mapParams) {

        //Busca os evcs utilizados
        String urlEvcs = configurationData.getUrlIntegrationManager("getEvcsSecondLayer");
        HttpEntity<String> httpEntityEvcs = IntegrationUtils.createHttpEntityForJSon(jsonRequest, mapParams);
        LOGGER.info("[Integration] Call evcs utilized " + urlEvcs + " --> " + httpEntityEvcs.getBody());

        List<EVCS> agregationEvcsList = new ArrayList<>();
        EVCS[] agregationEvcs;
        agregationEvcs = restTemplate.postForObject(urlEvcs, httpEntityEvcs, EVCS[].class);
        agregationEvcsList.addAll(Arrays.asList(agregationEvcs));

        List<ChargePointData> listEpNaoUtilizadosEvcs = compareListsEvcs(chargePointTotal, agregationEvcsList);

        //busca os v2gs utilizados
        String urlV2g = configurationData.getUrlIntegrationManager("getAllV2gSecondLayer");
        HttpEntity<String> httpEntityV2g = IntegrationUtils.createHttpEntityForJSon(jsonRequest, mapParams);
        LOGGER.info("[Integration] Call v2g utilized " + urlV2g + " --> " + httpEntityV2g.getBody());

        List<V2G> agregationV2GList = new ArrayList<>();
        V2G[] agregationV2G;
        agregationV2G = restTemplate.postForObject(urlV2g, httpEntityV2g, V2G[].class);
        agregationV2GList.addAll(Arrays.asList(agregationV2G));

        List<ChargePointData> listEpNaoUtilizadosV2g = compareListsV2g(listEpNaoUtilizadosEvcs, agregationV2GList);

        return listEpNaoUtilizadosV2g;
    }

    private List<ChargePointData> compareListsV2g(List<ChargePointData> listEpNaoUtilizadosEvcs, List<V2G> agregationEvcsList) {
        List<ChargePointData> listaNaoUsados = new ArrayList<>();

        Set<String> setB = new HashSet<>();
        for (V2G objB : agregationEvcsList) {
            setB.add(objB.getId());
        }
        for (ChargePointData objA : listEpNaoUtilizadosEvcs) {
            if (!setB.contains(objA.getDeviceIdentification())) {
                listaNaoUsados.add(objA);
            }
        }
        return listaNaoUsados;
    }

    private List<ChargePointData> compareListsEvcs(List<ChargePointData> chargePointTotal, List<EVCS> agregationEPS) {

        List<ChargePointData> listaNaoUsados = new ArrayList<>();

        Set<String> setB = new HashSet<>();
        for (EVCS objB : agregationEPS) {
            setB.add(objB.getId());
        }

        for (ChargePointData objA : chargePointTotal) {
            if (!setB.contains(objA.getDeviceIdentification())) {
                listaNaoUsados.add(objA);
            }
        }

        return listaNaoUsados;
    }

    /**
     * Busca todos os evcs cadastrados -- getEvcsSecondLayer
     *
     * @return dados dos evcs cadastrados
     */
    public GetEvcsSecondLayer findAllAgregationsEpsEvcs() throws ApiException {
        // Json de request
        String jsonRequest = "{}";

        // Parâmetros de request
        Map<String, String> mapParams = new HashMap<>();

        List<EVCS> agregationEPS = new ArrayList<>();
        GetEvcsSecondLayer getEvcsSecondLayer = new GetEvcsSecondLayer();

        try {
            String url = configurationData.getUrlIntegrationManager("getEvcsSecondLayer");
            HttpEntity<String> httpEntity = IntegrationUtils.createHttpEntityForJSon(jsonRequest, mapParams);
            LOGGER.info("[Integration] Call " + url + " --> " + httpEntity.getBody());

            EVCS[] agregation;
            agregation = restTemplate.postForObject(url, httpEntity, EVCS[].class);

            agregationEPS.addAll(Arrays.asList(agregation));
            getEvcsSecondLayer.setAgregationEpKey(agregationEPS);

            return getEvcsSecondLayer;

        } catch (Throwable e) {
            onError(e);
            return null;
        }

    }


    /**
     * Busca EVCS pelo id -- getIdEvcsSecondLayer
     *
     * @return dados dos setup cadastrado por id
     */
    public EVCS findById(String id) throws ApiException {

        // Json de request
        String jsonRequest = "{ \"id\": \":id\" } ";

        // Parâmetros de request
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put(":id", String.valueOf(id));

        try {
            String url = configurationData.getUrlIntegrationManager("getIdEvcsSecondLayer");
            HttpEntity<String> httpEntity = IntegrationUtils.createHttpEntityForJSon(jsonRequest, mapParams);
            LOGGER.info("[Integration] Call " + url + " --> " + httpEntity.getBody());

            EVCS evcs = restTemplate.postForObject(url, httpEntity, EVCS.class);

            return evcs;
        } catch (Throwable e) {
            onError(e);
            return null;
        }

    }

    public EVCS createEvcs(EVCS evcs) throws ApiException {

        // Parâmetros de request
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put(":id", evcs.getId());
        mapParams.put(":name", evcs.getName());
        mapParams.put(":setup_id", String.valueOf(evcs.getSetup_id()));
        mapParams.put(":nconn", String.valueOf(evcs.getNconn()));
        mapParams.put(":control", evcs.getControl());

        String jsonRequest = createJsonRequest(evcs, mapParams);

        try {
            String url = configurationData.getUrlIntegrationManager("createEvcsSecondLayer");
            HttpEntity<String> httpEntity = IntegrationUtils.createHttpEntityForJSon(jsonRequest, mapParams);
            LOGGER.info("[Integration] Call " + url + " --> " + httpEntity.getBody());

            EVCS evcsCreated = restTemplate.postForObject(url, httpEntity, EVCS.class);


            return evcsCreated;
        } catch (Throwable e) {
            onError(e);
            return null;
        }
    }

    private String createJsonRequest(EVCS evcs, Map<String, String> mapParams) {

        String jsonRequest = "{ "
                + "  \"id\": \":id\", "
                + "  \"name\": \":name\", "
                + "  \"setup_id\": :setup_id ,"
                + "  \"nconn\": :nconn ,"
                + "  \"control\": \":control\", ";

        if (evcs.getConn1_type() != null && !evcs.getConn1_type().isEmpty()) {
            jsonRequest += "  \"conn1_type\": \":conn1_type\" ";
            mapParams.put(":conn1_type", evcs.getConn1_type());
        }

        if (evcs.getConn1_Pmax() != null && evcs.getConn1_Pmax() > 0) {
            jsonRequest += ",  \"conn1_Pmax\": :conn1_Pmax ";
            Double pmax = evcs.getConn1_Pmax();
            mapParams.put(":conn1_Pmax", String.valueOf(evcs.getConn1_Pmax().floatValue()));
        }

        if (evcs.getConn1_Vnom() != null && evcs.getConn1_Vnom() > 0) {
            jsonRequest += ",  \"conn1_Vnom\": :conn1_Vnom ";
            Double vnom = evcs.getConn1_Vnom();
            mapParams.put(":conn1_Vnom", String.valueOf(evcs.getConn1_Vnom().floatValue()));
        }

        if (evcs.getConn1_Imax() != null && evcs.getConn1_Imax() > 0) {
            jsonRequest += ",  \"conn1_Imax\": :conn1_Imax ";
            Double imax = evcs.getConn1_Imax();
            mapParams.put(":conn1_Imax", String.valueOf(evcs.getConn1_Imax().floatValue()));
        }

        //conector 2
        if (evcs.getConn2_type() != null && !evcs.getConn2_type().isEmpty()) {
            jsonRequest += ",  \"conn2_type\": \":conn2_type\" ";
            mapParams.put(":conn2_type", evcs.getConn2_type());
        }

        if (evcs.getConn2_Pmax() != null && evcs.getConn2_Pmax() > 0) {
            jsonRequest += ",  \"conn2_Pmax\": :conn2_Pmax ";
            mapParams.put(":conn2_Pmax", String.valueOf(evcs.getConn2_Pmax().floatValue()));
        }

        if (evcs.getConn2_Vnom() != null && evcs.getConn2_Vnom() > 0) {
            jsonRequest += ",  \"conn2_Vnom\": :conn2_Vnom ";
            mapParams.put(":conn2_Vnom", String.valueOf(evcs.getConn2_Vnom().floatValue()));
        }

        if (evcs.getConn2_Imax() != null && evcs.getConn2_Imax() > 0) {
            jsonRequest += ",  \"conn2_Imax\": :conn2_Imax ";
            mapParams.put(":conn2_Imax", String.valueOf(evcs.getConn2_Imax().floatValue()));
        }

        //conector 3
        if (evcs.getConn3_type() != null && !evcs.getConn3_type().isEmpty()) {
            jsonRequest += ",  \"conn3_type\": \":conn3_type\" ";
            mapParams.put(":conn3_type", evcs.getConn3_type());
        }

        if (evcs.getConn3_Pmax() != null && evcs.getConn3_Pmax() > 0) {
            jsonRequest += ",  \"conn3_Pmax\": :conn3_Pmax ";
            mapParams.put(":conn3_Pmax", String.valueOf(evcs.getConn3_Pmax().floatValue()));
        }

        if (evcs.getConn3_Vnom() != null && evcs.getConn3_Vnom() > 0) {
            jsonRequest += ",  \"conn3_Vnom\": :conn3_Vnom ";
            mapParams.put(":conn3_Vnom", String.valueOf(evcs.getConn3_Vnom().floatValue()));
        }

        if (evcs.getConn3_Imax() != null && evcs.getConn3_Imax() > 0) {
            jsonRequest += ",  \"conn3_Imax\": :conn3_Imax ";
            mapParams.put(":conn3_Imax", String.valueOf(evcs.getConn3_Imax().floatValue()));
        }

        jsonRequest += " } ";

        return jsonRequest;
    }

    public EVCS updateEVCS(EVCS evcsUpdated) throws ApiException {

        // Parâmetros de request
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put(":id", evcsUpdated.getId());
        mapParams.put(":name", evcsUpdated.getName());
        mapParams.put(":setup_id", String.valueOf(evcsUpdated.getSetup_id()));
        mapParams.put(":nconn", String.valueOf(evcsUpdated.getNconn()));
        mapParams.put(":control", evcsUpdated.getControl());

        String jsonRequest = createJsonRequest(evcsUpdated, mapParams);

        try {
            String url = configurationData.getUrlIntegrationManager("putEvcsSecondLayer");
            HttpEntity<String> httpEntity = IntegrationUtils.createHttpEntityForJSon(jsonRequest, mapParams);
            LOGGER.info("[Integration] Call " + url + " --> " + httpEntity.getBody());

            EVCS evcsUp = restTemplate.postForObject(url, httpEntity, EVCS.class);
            evcsUp.setId(evcsUpdated.getId());
            return evcsUp;
        } catch (Throwable e) {
            onError(e);
            return null;
        }

    }

    public void deleteEVCS(String id) throws ApiException {

        // Json de request
        String jsonRequest = "{ \"id\": \":id\" } ";

        // Parâmetros de request
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put(":id", String.valueOf(id));

        try {
            String url = configurationData.getUrlIntegrationManager("deleteEvcsSecondLayer");
            HttpEntity<String> httpEntity = IntegrationUtils.createHttpEntityForJSon(jsonRequest, mapParams);
            LOGGER.info("[Integration] Call " + url + " --> " + httpEntity.getBody());

            restTemplate.postForObject(url, httpEntity, EVCS.class);
        } catch (Throwable e) {
            onError(e);
        }
    }


    /**
     * Tratamento de erros para integração com o manager
     *
     * @param e
     * @throws ApiException
     */
    private void onError(Throwable e) throws ApiException {

        if (e instanceof HttpClientErrorException) {
            LOGGER.error("[Manager-integration] Ocorreu um erro na chamada da integra��o. Erro {}", ((HttpClientErrorException) e).getStatusCode());
            throw new BusinessException(ExceptionConstant.valueOf(e.getMessage()));

        } else if (e instanceof ResourceAccessException) {
            if (e.getCause() instanceof SocketTimeoutException) {
                LOGGER.error("[Manager-integration] Ocorreu um timeout na chamada da integra��o.");
                throw new BusinessException(ExceptionConstant.INTEGRATION_TIMEOUT_ERROR);
            }
            LOGGER.error("[Manager-integration] Ocorreu um erro na chamada da integra��o. Erro {}", e.getMessage());
            throw new BusinessException(ExceptionConstant.INTEGRATION_HOST_ERROR);

        } else {
            LOGGER.error("[Manager-integration] Ocorreu um erro na leitura dos dados da integra��o.", e);
            throw new BusinessException(ExceptionConstant.INTEGRATION_HOST_ERROR);
        }
    }


}
