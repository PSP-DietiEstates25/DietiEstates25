package com.authorizationserver.api.config;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	@Value("${security.cors.allowed-origin:http://localhost:4200}")
	private String allowedOrigin;
	
	@Bean
	@Order(1)
	public SecurityFilterChain asFilterChain(HttpSecurity http) throws Exception {
		
		//http.with(OAuth2AuthorizationServerConfigurer.authorizationServer(), Customizer.withDefaults());
		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
		http.cors(Customizer.withDefaults());
		http.getConfigurer(OAuth2AuthorizationServerConfigurer.class).oidc(Customizer.withDefaults());
		http.exceptionHandling((e) -> e.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("http://localhost:4200/auth/login")));
		
		return http.build();
	}
	
	@Bean
	@Order(2)
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
	
		http.formLogin(Customizer.withDefaults());
		//http.sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.authorizeHttpRequests(
				c -> c.anyRequest().authenticated()
				);
		return http.build();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		var userDetails = User.withUsername("bill")
				.password("password")
				.roles("USER")
				.build();
		
		return new InMemoryUserDetailsManager(userDetails);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	
	@Bean
	public RegisteredClientRepository registeredClientRepository() {
		RegisteredClient registeredClient = RegisteredClient
				.withId(UUID.randomUUID().toString())
				.clientId("angularspa")
				.clientSecret("secret")
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.redirectUri("http://localhost:4200/search")
				.scope(OidcScopes.OPENID)
				.build();
		
		return new InMemoryRegisteredClientRepository(registeredClient);
	}
	
	@Bean
	public JWKSource<SecurityContext> jwkSource() throws NoSuchAlgorithmException {
		
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		
		keyPairGenerator.initialize(2048);
		
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		
		RSAKey rsaKey = new RSAKey.Builder(publicKey)
				.privateKey(privateKey)
				.keyID(UUID.randomUUID().toString())
				.build();
		
		JWKSet jwkSet = new JWKSet(rsaKey);
		return new ImmutableJWKSet<>(jwkSet);
	}
	
	@Bean
	public AuthorizationServerSettings authorizationServerSettings() {

		return AuthorizationServerSettings.builder().build();
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

	    // consenti esplicitamente la tua SPA
	    configuration.setAllowedOrigins(List.of("http://localhost:4200"));

	    // consenti tutti gli header necessari
	    configuration.setAllowedHeaders(List.of("*"));

	    // consenti tutti i metodi (GET, POST, PUT, DELETE, OPTIONS, ...)
	    configuration.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS","PATCH"));

	    // permette l'invio di cookie / credenziali (se non ti servono metti false)
	    configuration.setAllowCredentials(true);

	    // tempo di cache preflight (in secondi)
	    configuration.setMaxAge(3600L);

	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    // ATTENZIONE: qui registro /** per abilitare tutti i path
	    source.registerCorsConfiguration("/**", configuration);
	    return source;
	}
	
	/*
	private final AuthenticationProvider authenticationProvider;

	@Value("${security.cors.allowed-origin:http://localhost:4200}")
	private String allowedOrigin;
	
	@Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcRegisteredClientRepository(jdbcTemplate);
    }

    @Bean
    public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
    }
    
    @Bean
	public CorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		config.addAllowedOrigin("http://127.0.0.1:4200");
		config.setAllowCredentials(true);
		source.registerCorsConfiguration("/**", config);
		return source;
	}
	/*
	@Bean
	@Order(1)
	public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
		return http.cors(Customizer.withDefaults()).build();
	}
	
	@Bean
	@Order(2)
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		return http.cors(Customizer.withDefaults()).build();
	}
	*/
	
	
	
	
	
	
	/*
	 @Value("${security.cors.allowed-origin:http://localhost:4200}")
	private String allowedOrigin;
	
	@Bean
	@Order(1)
	public SecurityFilterChain asFilterChain(HttpSecurity http) throws Exception {
		
		//http.with(OAuth2AuthorizationServerConfigurer.authorizationServer(), Customizer.withDefaults());
		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
		http.getConfigurer(OAuth2AuthorizationServerConfigurer.class).oidc(Customizer.withDefaults());
		http.exceptionHandling((e) -> e.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("http://127.0.0.1:4200/auth/login")));
		
		return http.build();
	}
	
	@Bean
	@Order(2)
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
	
		http.formLogin(Customizer.withDefaults());
		http.authorizeHttpRequests(
				c -> c.anyRequest().authenticated()
				);
		
		return http.build();
	}
	
	@Bean
	public RegisteredClientRepository registeredClientRepository() {
		RegisteredClient registeredClient = RegisteredClient
				.withId(UUID.randomUUID().toString())
				.clientId("client")
				.clientSecret("secret")
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.redirectUri("http://127.0.0.1:4200/")
				.scope(OidcScopes.OPENID)
				.build();
		
		return new InMemoryRegisteredClientRepository(registeredClient);
	}
	
	@Bean
	public JWKSource<SecurityContext> jwkSource() throws NoSuchAlgorithmException {
		
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		
		keyPairGenerator.initialize(2048);
		
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		
		RSAKey rsaKey = new RSAKey.Builder(publicKey)
				.privateKey(privateKey)
				.keyID(UUID.randomUUID().toString())
				.build();
		
		JWKSet jwkSet = new JWKSet(rsaKey);
		return new ImmutableJWKSet<>(jwkSet);
	}
	
	@Bean
	public AuthorizationServerSettings authorizationServerSettings() {

		return AuthorizationServerSettings.builder().build();
	}
	*/
}
