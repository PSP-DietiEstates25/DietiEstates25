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
 * - mantiene i tuoi mixin e (de)serializer per DefaultAccount/SecurityAccount/Role
 * - registra tutti i moduli Spring Security necessari (incluso oauth2-client)
 * - espone un ObjectMapper @Primary con tutti i moduli registrati
 */
@Configuration
public class JacksonConfig {

    // ====== MIXIN & (DE)SERIALIZER PERSONALIZZATI ======

    @Bean
    public Module securityMixinsModule() {
        SimpleModule m = new SimpleModule();
        m.setMixInAnnotation(com.dietiestates.auth.model.SecurityAccount.class, SecurityAccountDecoratorMixin.class);
        m.setMixInAnnotation(com.dietiestates.auth.model.DefaultAccount.class, DefaultAccountMixin.class);
        m.setMixInAnnotation(com.dietiestates.auth.model.Role.class, RoleMixin.class);
        m.addDeserializer(SimpleGrantedAuthority.class, new SimpleGrantedAuthorityDeserializer());
        m.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        m.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        return m;
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
        public SimpleGrantedAuthority deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode node = p.getCodec().readTree(p);
            String authority = node.get("authority").asText();
            return new SimpleGrantedAuthority(authority);
        }
    }

    public static class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
        private static final DateTimeFormatter F = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        @Override public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.format(F));
        }
    }

    public static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        private static final DateTimeFormatter F = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        @Override public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return LocalDateTime.parse(p.getValueAsString(), F);
        }
    }

    // ====== MODULI SPRING SECURITY / OAUTH2 / WEB / CLIENT ======

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

    /** Modulo chiave per la allowlist di OAuth2AuthenticationToken (login Google) */
    @Bean
    public Module oauth2ClientJackson2Module() {
        return new org.springframework.security.oauth2.client.jackson2.OAuth2ClientJackson2Module();
    }

    /**
     * ObjectMapper primario con TUTTI i moduli registrati.
     * - registra i @Bean Module dichiarati in questo config
     * - aggiunge anche il “bundle” SecurityJackson2Modules
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper(List<Module> declaredModules) {
        ObjectMapper mapper = new ObjectMapper();

        // 1) Moduli dichiarati come @Bean (mixin, JavaTime, Core, AuthServer, Web, Client, ecc.)
        for (Module m : declaredModules) {
            mapper.registerModule(m);
        }

        // 2) Bundle Spring Security (copre altre classi allowlisted)
        for (Module m : SecurityJackson2Modules.getModules(getClass().getClassLoader())) {
            mapper.registerModule(m);
        }

        return mapper;
    }
}
