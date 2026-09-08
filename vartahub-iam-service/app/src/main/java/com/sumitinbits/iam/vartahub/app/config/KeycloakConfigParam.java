package com.sumitinbits.iam.vartahub.app.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "keycloak.config")
@Getter
@Setter
public class KeycloakConfigParam {
    private String adminUsername;
    private String adminPassword;
    private String iamClientSecret;
    private String googleProviderClientId;
    private String googleProviderClientSecret;
    private String smtpHost;
    private String smtpPort;
    private String smtpUser;
    private String smtpPassword;
}
