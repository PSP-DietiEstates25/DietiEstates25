package com.dietiestates.auth.config;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.jackson2.CoreJackson2Module;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.web.jackson2.WebServletJackson2Module;

/**
 * Jackson configuration:
 * mantiene mixins per defaultAccount, securityAccount, role
 * registra tutti i moduli di spring security
 * espone un objectMapper @Primary con tutti i moduli registrati
 */
@Configuration
public class JacksonConfig {

    @Bean
    public Module securityMixinsModule() {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.setMixInAnnotation(com.dietiestates.auth.model.SecurityAccount.class, SecurityAccountDecoratorMixin.class);
        simpleModule.setMixInAnnotation(com.dietiestates.auth.model.DefaultAccount.class, DefaultAccountMixin.class);
        simpleModule.setMixInAnnotation(com.dietiestates.auth.model.Role.class, RoleMixin.class);
        simpleModule.addDeserializer(SimpleGrantedAuthority.class, new SimpleGrantedAuthorityDeserializer());
        simpleModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        simpleModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        return simpleModule;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
            getterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY,
            isGetterVisibility = JsonAutoDetect.Visibility.NONE,
            setterVisibility = JsonAutoDetect.Visibility.ANY)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public abstract static class SecurityAccountDecoratorMixin {
        @JsonCreator
        public SecurityAccountDecoratorMixin(
                @JsonProperty("defaultAccount") com.dietiestates.auth.model.DefaultAccount defaultAccount,
                @JsonProperty("enabled") Boolean enabled,
                @JsonProperty("locked") Boolean locked,
                @JsonProperty("tempAuthorities") Object tempAuthorities) {}
        @JsonProperty("authorities")
        public abstract void setAuthorities(Collection<?> authorities);
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
            getterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY,
            isGetterVisibility = JsonAutoDetect.Visibility.NONE,
            setterVisibility = JsonAutoDetect.Visibility.ANY)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public abstract static class DefaultAccountMixin {
        @JsonCreator
        public DefaultAccountMixin(
                @JsonProperty("id") Long id,
                @JsonProperty("email") String email,
                @JsonProperty("password") String password,
                @JsonProperty("createdDate") LocalDateTime createdDate,
                @JsonProperty("lastModifiedDate") LocalDateTime lastModifiedDate,
                @JsonProperty("role") com.dietiestates.auth.model.Role role) {}
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
            getterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY,
            isGetterVisibility = JsonAutoDetect.Visibility.NONE,
            setterVisibility = JsonAutoDetect.Visibility.ANY)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public abstract static class RoleMixin {
        @JsonCreator
        public RoleMixin(
                @JsonProperty("id") Integer id,
                @JsonProperty("name") com.dietiestates.auth.enums.RoleName name,
                @JsonProperty("createdDate") LocalDateTime createdDate,
                @JsonProperty("lastModifiedDate") LocalDateTime lastModifiedDate) {}
    }

    public static class SimpleGrantedAuthorityDeserializer extends JsonDeserializer<SimpleGrantedAuthority> {
        @Override
        public SimpleGrantedAuthority deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            String authority = node.get("authority").asText();
            return new SimpleGrantedAuthority(authority);
        }
    }

    public static class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
        private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        @Override public void serialize(LocalDateTime value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(value.format(dateTimeFormatter));
        }
    }

    public static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        @Override public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            return LocalDateTime.parse(jsonParser.getValueAsString(), dateTimeFormatter);
        }
    }

    @Bean
    public Module springSecurityCoreModule() {
        return new CoreJackson2Module();
    }

    @Bean
    public Module oauth2AuthServerModule() {
        return new OAuth2AuthorizationServerJackson2Module();
    }

    @Bean
    public Module webServletJackson2Module() {
        return new WebServletJackson2Module();
    }

    @Bean
    public Module javaTimeModule() {
        return new JavaTimeModule();
    }

    @Bean
    public Module oauth2ClientJackson2Module() {
        return new org.springframework.security.oauth2.client.jackson2.OAuth2ClientJackson2Module();
    }

    /**
     * ObjectMapper primario con TUTTI i moduli registrati.
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper(List<Module> declaredModules) {
        ObjectMapper mapper = new ObjectMapper();

        for (Module module : declaredModules) {
            mapper.registerModule(module);
        }

        for (Module module : SecurityJackson2Modules.getModules(getClass().getClassLoader())) {
            mapper.registerModule(module);
        }

        return mapper;
    }
}
