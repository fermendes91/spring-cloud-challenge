package br.com.alura.microservice.eatsconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class EatsConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EatsConfigServerApplication.class, args);
	}

}
