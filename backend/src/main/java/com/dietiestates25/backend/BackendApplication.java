package com.dietiestates25.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.dietiestates25.backend.model.Esempio;

@SpringBootApplication
public class BackendApplication {
	
	private static final Logger log = LoggerFactory.getLogger(BackendApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner demo() {
		return (args) -> {
			Esempio e1 = new Esempio();
			Esempio e2 = new Esempio("ciao", "bello");
			
			log.info("----------ESEMPIO DEMONSTRATION----------");
			log.info(e1.toString());
			log.info(e2.toString());
			e1.setName("luca");
			e1.setPassword("miapass");
			log.info(e1.toString());
		};
	}
}
