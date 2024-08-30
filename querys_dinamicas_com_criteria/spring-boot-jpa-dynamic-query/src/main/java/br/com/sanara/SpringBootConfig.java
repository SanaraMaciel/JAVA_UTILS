package br.com.sanara;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class SpringBootConfig {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(SpringBootConfig.class, args);
	}
}
