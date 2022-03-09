package br.com.caelum.eats.distancia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class EatsDistanciaServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EatsDistanciaServiceApplication.class, args);
	}

}
