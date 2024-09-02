package br.com.sanara.integration.viacep.route;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import br.com.sanara.integration.viacep.ApiIntegrationViaCepApplication;
import br.com.sanara.integration.viacep.entity.Endereco;

@SpringBootTest(
		classes = ApiIntegrationViaCepApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CamelSpringBootTest
class EnderecoRouteTest {

	final String uriT = "http://localhost:%s/pcri/viacep/endereco/%s";
	
    @LocalServerPort
    private int serverPort;

    @Autowired
    private TestRestTemplate testRestTemplate;
	
    @Test
    void givenValidCep_cepUri_WillReturnEnderecoData() {
        String cep = "31580-230";
        String uri = format(uriT, serverPort, cep);
        Endereco endereco = testRestTemplate.getForObject(uri, Endereco.class);
        assertNotNull(endereco);
        assertEquals(cep, endereco.getCep());
    }

    @Test
    void givenInValidCep_cepUri_WillReturn404() {
        String cep = "31580-23000";
        String uri = format(uriT, serverPort, cep);
        ResponseEntity<Object> endereco = testRestTemplate.getForEntity(uri, Object.class);
        assertNotNull(endereco);
        assertEquals(404, endereco.getStatusCode().value());
    }
    
}
