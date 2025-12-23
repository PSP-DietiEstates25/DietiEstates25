package com.dietiestates.auth.config;

import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Connector;

import org.springframework.boot.tomcat.servlet.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!test")
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class TomcatServerConfig {

    private final AuthorizationServerProperties properties;

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> connectorCustomizer() {
        return (tomcat) -> tomcat.addAdditionalConnectors(createHttpConnector());
    }

    private Connector createHttpConnector() {
        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        connector.setScheme("http");
        connector.setPort(properties.authorizationServerBaseHttpPort());
        connector.setSecure(false);
        connector.setRedirectPort(properties.authorizationServerBaseHttpsPort());
        return connector;
    }
}
