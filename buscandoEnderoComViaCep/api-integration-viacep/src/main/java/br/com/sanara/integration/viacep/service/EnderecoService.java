package br.com.sanara.integration.viacep.service;

import br.com.sanara.integration.viacep.entity.Endereco;
import br.com.sanara.integration.viacep.exception.ViaCepException;
import br.com.sanara.integration.viacep.utils.EnderecoUtils;

import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.MINUTES;

@Service
public class EnderecoService {

    private static final String VIACEPURL = "https://viacep.com.br/ws/";
    private static final Gson gson = new Gson();

    public Endereco findEnderecoByCep(String cep) throws IOException, InterruptedException {
    	
    	EnderecoUtils.validaCep(cep);

        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.of(1, MINUTES))
                .build();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(VIACEPURL + cep + "/json"))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if(httpResponse.statusCode() == 400)
            throw new ViaCepException("Endereco não encontrado.");

        return gson.fromJson(httpResponse.body(), Endereco.class);

    }
}