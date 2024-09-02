package br.com.sanara.integration.viacep;

import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.Servlet;

@SpringBootApplication
public class ApiIntegrationViaCepApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiIntegrationViaCepApplication.class);
	}

	@Bean
	public ServletRegistrationBean<Servlet> servletRegistrationBean() {

		ServletRegistrationBean<Servlet> registration = new ServletRegistrationBean<>(
				new CamelHttpTransportServlet(), "/pcri/viacep/*");
		registration.setName("CamelServlet");
		return registration;
	}

}
