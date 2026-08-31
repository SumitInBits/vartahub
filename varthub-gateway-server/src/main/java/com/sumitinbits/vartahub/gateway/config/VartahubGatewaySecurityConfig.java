package com.sumitinbits.vartahub.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class VartahubGatewaySecurityConfig {


    /**
     * Configures the primary security filter chain for the gateway.
     *
     * This configuration:
     * - Disables CSRF protection for stateless APIs
     * - Enables CORS using a custom configuration source
     * - Denies access to internal API endpoints
     * - Requires authentication for all /api/hask/** endpoints
     * - Denies all other requests by default
     * - Configures the application as an OAuth2 Resource Server using JWT
     *
     * @param http the HttpSecurity to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if a security configuration error occurs
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain gatewaySecurityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("Gateway Security Initialized!!!");
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, getPublicEndpoints()).permitAll()
                        .requestMatchers(HttpMethod.POST, postPublicEndpoints()).permitAll()
                        .requestMatchers("/api/vartahub/*/*/private/**").denyAll()
                        .requestMatchers("/api/vartahub/*/**").authenticated()
                        .anyRequest().denyAll()
                )
                .oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults()));

        return http.build();
    }

    /**
     * Defines the CORS configuration for the gateway.
     *
     * This configuration:
     * - Allows requests from specified origin patterns (e.g., localhost frontend)
     * - Permits common HTTP methods (GET, POST, PUT, DELETE, OPTIONS)
     * - Allows all headers
     * - Enables credentials such as cookies and authorization headers
     *
     * @return the configured CorsConfigurationSource
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    public String[] getPublicEndpoints() {
        return new String[] {
                "/api/vartahub/iam/v1/specialisations"
        };
    }

    public String[] postPublicEndpoints() {
        return new String[] {
                "/api/vartahub/iam/v1/users"
        };
    }
}
