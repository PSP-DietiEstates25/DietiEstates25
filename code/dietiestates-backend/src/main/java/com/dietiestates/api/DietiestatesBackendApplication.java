package com.dietiestates.api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dietiestates.api.model.Admin;
import com.dietiestates.api.model.DefaultAccount;
import com.dietiestates.api.model.Role;
import com.dietiestates.api.model.SecurityAccountDecorator;
import com.dietiestates.api.repository.AdminRepository;
import com.dietiestates.api.repository.DefaultAccountRepository;
import com.dietiestates.api.repository.RoleRepository;

@SpringBootApplication
@EnableJpaAuditing
public class DietiestatesBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(DietiestatesBackendApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(
			RoleRepository roleRepository,
			DefaultAccountRepository defaultAccountRepository,
			AdminRepository adminRepository,
			PasswordEncoder passwordEncoder
			) {
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
			
			if(defaultAccountRepository.findByEmail("admin@admin.com").isEmpty()) {

				var defaultAccount = DefaultAccount.builder()
						.email("admin@admin.com")
						.password(passwordEncoder.encode("adminpassword"))
						.role(roleRepository.findByName("ADMIN").get())
						.build();
				var securityAccountDecorator = SecurityAccountDecorator.builder()
						.defaultAccount(defaultAccount)
						.enabled(true)
						.locked(false)
						.build();
				var admin = new Admin();
				admin.setSecurityAccountDecorator(defaultAccount);
				admin.setAdmin(admin);
				
				defaultAccountRepository.save(defaultAccount);
				adminRepository.save(admin);
			}
		};
	}
}
