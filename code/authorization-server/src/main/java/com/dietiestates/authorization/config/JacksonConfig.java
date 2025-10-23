package com.dietiestates.authorization.config;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.jackson2.CoreJackson2Module;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.web.jackson2.WebServletJackson2Module;

import com.dietiestates.authorization.enums.RoleName;
import com.dietiestates.authorization.model.DefaultAccount;
import com.dietiestates.authorization.model.Role;
import com.dietiestates.authorization.model.SecurityAccountDecorator;
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
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Configuration
public class JacksonConfig {

    /** Modulo con i tuoi mixin/custom serializer (per le tue classi) */
    @Bean
    public Module securityMixinsModule() {
        SimpleModule m = new SimpleModule();
        m.setMixInAnnotation(SecurityAccountDecorator.class, SecurityAccountDecoratorMixin.class);
        m.setMixInAnnotation(DefaultAccount.class, DefaultAccountMixin.class);
        m.setMixInAnnotation(Role.class, RoleMixin.class);
        m.addDeserializer(SimpleGrantedAuthority.class, new SimpleGrantedAuthorityDeserializer());
        m.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        m.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        return m;
    }

    /** Moduli ufficiali Spring Security + Authorization Server */
    @Bean
    public Module springSecurityCoreModule() {
        return new CoreJackson2Module();
    }

    @Bean
    public Module oauth2AuthServerModule() {
        return new OAuth2AuthorizationServerJackson2Module();
    }

    /** IMPORTANTISSIMO: modulo “web” per serializzare/deserialize WebAuthenticationDetails, ecc. */
    @Bean
    public Module webServletJackson2Module() {
        return new WebServletJackson2Module();
    }

    // ====== MIXIN & (DE)SERIALIZER ======

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
            getterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY,
            isGetterVisibility = JsonAutoDetect.Visibility.NONE,
            setterVisibility = JsonAutoDetect.Visibility.ANY)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public abstract static class SecurityAccountDecoratorMixin {
        @JsonCreator
        public SecurityAccountDecoratorMixin(
                @JsonProperty("defaultAccount") DefaultAccount defaultAccount,
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
                @JsonProperty("role") Role role) {}
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
                @JsonProperty("name") RoleName name,
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
}
