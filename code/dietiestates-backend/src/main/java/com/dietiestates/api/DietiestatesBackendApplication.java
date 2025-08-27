package com.dietiestates.api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.dietiestates.api.model.Role;
import com.dietiestates.api.repository.RoleRepository;

@SpringBootApplication
@EnableJpaAuditing
public class DietiestatesBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(DietiestatesBackendApplication.class, args);
	}

	//command line runner per la creazione di un user
	@Bean
	public CommandLineRunner runner(RoleRepository roleRepository) {
		return _ -> {
			if(roleRepository.findByName("USER").isEmpty()) {
				roleRepository.save(
						Role.builder().name("USER").build()
						);
			}
			
			if(roleRepository.findByName("ADMIN").isEmpty()) {
				roleRepository.save(
						Role.builder().name("ADMIN").build()
						);
			}
			
			if(roleRepository.findByName("ESTATE_AGENT").isEmpty()) {
				roleRepository.save(
						Role.builder().name("ESTATE_AGENT").build()
						);
			}
		};
	}
}
