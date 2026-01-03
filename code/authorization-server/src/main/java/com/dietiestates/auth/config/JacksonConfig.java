package com.dietiestates.auth.config;

import com.dietiestates.auth.enums.RoleName;
import com.dietiestates.auth.model.DefaultAccount;
import com.dietiestates.auth.model.Role;
import com.dietiestates.auth.model.SecurityAccount;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.jackson2.CoreJackson2Module;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.client.jackson2.OAuth2ClientJackson2Module;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.web.jackson2.WebServletJackson2Module;
import com.fasterxml.jackson.databind.Module;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    public ObjectMapper webObjectMapper() {
        ObjectMapper mapper = JsonMapper.builder().build();
        mapper.findAndRegisterModules();
        mapper.deactivateDefaultTyping();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    @Bean
    public Module securityMixinsModule() {
        SimpleModule simpleModule = new SimpleModule();

        simpleModule.setMixInAnnotation(SecurityAccount.class, SecurityAccountDecoratorMixin.class);
        simpleModule.setMixInAnnotation(DefaultAccount.class, DefaultAccountMixin.class);
        simpleModule.setMixInAnnotation(Role.class, RoleMixin.class);

        simpleModule.addDeserializer(SimpleGrantedAuthority.class, new SimpleGrantedAuthorityDeserializer());

        simpleModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        simpleModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());

        return simpleModule;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
    @JsonAutoDetect(
            fieldVisibility = JsonAutoDetect.Visibility.ANY,
            getterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY,
            isGetterVisibility = JsonAutoDetect.Visibility.NONE,
            setterVisibility = JsonAutoDetect.Visibility.ANY
    )
    @JsonIgnoreProperties(ignoreUnknown = true)
    public abstract static class SecurityAccountDecoratorMixin {

        @JsonCreator
        public SecurityAccountDecoratorMixin(
                @JsonProperty("defaultAccount") DefaultAccount defaultAccount,
                @JsonProperty("enabled") Boolean enabled,
                @JsonProperty("locked") Boolean locked,
                @JsonProperty("tempAuthorities") Object tempAuthorities
        ) { }

        @JsonProperty("authorities")
        public abstract void setAuthorities(Collection<?> authorities);
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
    @JsonAutoDetect(
            fieldVisibility = JsonAutoDetect.Visibility.ANY,
            getterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY,
            isGetterVisibility = JsonAutoDetect.Visibility.NONE,
            setterVisibility = JsonAutoDetect.Visibility.ANY
    )
    @JsonIgnoreProperties(ignoreUnknown = true)
    public abstract static class DefaultAccountMixin {

        @JsonCreator
        public DefaultAccountMixin(
                @JsonProperty("id") Long id,
                @JsonProperty("email") String email,
                @JsonProperty("password") String password,
                @JsonProperty("createdDate") LocalDateTime createdDate,
                @JsonProperty("lastModifiedDate") LocalDateTime lastModifiedDate,
                @JsonProperty("role") Role role
        ) { }
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
    @JsonAutoDetect(
            fieldVisibility = JsonAutoDetect.Visibility.ANY,
            getterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY,
            isGetterVisibility = JsonAutoDetect.Visibility.NONE,
            setterVisibility = JsonAutoDetect.Visibility.ANY
    )
    @JsonIgnoreProperties(ignoreUnknown = true)
    public abstract static class RoleMixin {

        @JsonCreator
        public RoleMixin(
                @JsonProperty("id") Integer id,
                @JsonProperty("name") RoleName name,
                @JsonProperty("createdDate") LocalDateTime createdDate,
                @JsonProperty("lastModifiedDate") LocalDateTime lastModifiedDate
        ) { }
    }

    public static class SimpleGrantedAuthorityDeserializer extends JsonDeserializer<SimpleGrantedAuthority> {
        @Override
        public SimpleGrantedAuthority deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode node = p.getCodec().readTree(p);
            JsonNode auth = node.get("authority");
            return new SimpleGrantedAuthority(auth != null ? auth.asText() : node.asText());
        }
    }

    public static class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
        private static final DateTimeFormatter F = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.format(F));
        }
    }

    public static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        private static final DateTimeFormatter F = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return LocalDateTime.parse(p.getValueAsString(), F);
        }
    }

    @Bean public Module springSecurityCoreModule() { return new CoreJackson2Module(); }

    @Bean public Module oauth2AuthServerModule() { return new OAuth2AuthorizationServerJackson2Module(); }

    @Bean public Module webServletJackson2Module() { return new WebServletJackson2Module(); }

    @Bean public Module javaTimeModule() { return new JavaTimeModule(); }

    @Bean public Module oauth2ClientJackson2Module() { return new OAuth2ClientJackson2Module(); }

    @Bean(name = "authorizationObjectMapper")
    public ObjectMapper authorizationObjectMapper(List<Module> declaredModules) {
        ObjectMapper mapper = JsonMapper.builder().build();

        for (Module module : declaredModules) {
            mapper.registerModule(module);
        }

        for (Module module : SecurityJackson2Modules.getModules(getClass().getClassLoader())) {
            mapper.registerModule(module);
        }

        return mapper;
    }
}
