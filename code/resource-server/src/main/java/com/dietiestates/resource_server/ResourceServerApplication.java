package com.dietiestates.resource_server;

import com.dietiestates.resource_server.config.ResourceServerProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties(ResourceServerProperties.class)
public class ResourceServerApplication {
	static void main(String[] args) {
		SpringApplication.run(ResourceServerApplication.class, args);
	}
}
