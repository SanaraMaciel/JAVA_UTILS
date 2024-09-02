package br.com.sanara.integration.viacep.route;

import br.com.sanara.integration.viacep.entity.Endereco;
import br.com.sanara.integration.viacep.exception.ViaCepException;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class EnderecoRoute extends RouteBuilder {

    private final Environment env;

    public EnderecoRoute(Environment env) {
        this.env = env;
    }

    public void configure() throws Exception {

        restConfiguration()
                .port(env.getProperty("server.port"))
                .bindingMode(RestBindingMode.json)
                .apiContextPath("/swagger/api-docs")
                .apiContextRouteId("routeSwaggerApiDocs")
                .apiProperty("api.title", "CPqD PCRI - Integração ViaCEP")
                .apiProperty("api.version", "1.0")
                .apiProperty("api.description", "Documentação da API de integração entre o CPqD PCRI e o ViaCEP para os serviços disponibilizados por esta fonte externa. Uma série de serviços são disponibilizados e utilizados pela aplicação CPqD PCRI em seu fluxo de execução.")
                .apiProperty("api.termsOfService", "https://www.cpqd.com.br")
                .apiProperty("api.contact.name", "CPqD")
                .apiProperty("api.contact.email", "cpqd@cpqd.com.br")
                .apiProperty("base.path", "/pcri/viacep")
                .apiProperty("schemes", "http,https")
                .apiProperty("cors", "true")
                .enableCORS(true)
                .corsAllowCredentials(true)
                .corsHeaderProperty("Access-Control-Allow-Origin","*")
                .corsHeaderProperty("Access-Control-Allow-Headers","Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Authorization");

        onException(ViaCepException.class)
                .handled(true)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(404))
                .setHeader(Exchange.CONTENT_TYPE, constant("text/json"))
                .setBody(exceptionMessage());

        rest("/cep")
                .tag("Endereco")
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .produces(MediaType.APPLICATION_JSON_VALUE)
                .get("/{cep}")
                .outType(Endereco.class)
                .description("Serviço desenvolvido para obter dados de endereço de um CEP")
                .param().name("cep").description("O cep do Endereço procurado").endParam()
                .responseMessage().code(200).message("Sucesso").endResponseMessage()
                .responseMessage().code(404).message("Endereço não encontrado").endResponseMessage()
                .responseMessage().code(500).message("Erro Interno no Servidor").endResponseMessage()
                .to("bean:enderecoService?method=findEnderecoByCep(${header.cep})");
    }
}
