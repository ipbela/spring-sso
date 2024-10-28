package com.example.Gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Autowired
    private ReactiveClientRegistrationRepository clientRegistrationRepository;

    @Value("${spring.security.oauth2.client.provider.azure.jwk-set-uri}")
    private String jwkSetUri;

    @Value("https://login.microsoftonline.com/0ae51e19-07c8-4e4b-bb6d-648ee58410f4/oauth2/v2.0/logout?post_logout_redirect_uri=http://localhost:5173")  // Ex: URI de logout do Azure
    private String logoutURI;

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(ServerHttpSecurity.CorsSpec::disable)
                .authorizeExchange(conf -> conf
                        .pathMatchers("/").permitAll()
                        .anyExchange().authenticated())
                .oauth2Login(conf -> conf
                        .authenticationSuccessHandler(new RedirectServerAuthenticationSuccessHandler("http://localhost:5173/profile")))
                .oauth2ResourceServer(conf -> conf
                        .jwt(jwt -> jwt.jwtDecoder(jwtDecoder())))
                .logout(logout -> logout
                        .logoutSuccessHandler(azureLogoutSuccessHandler()));

        return http.build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        return NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    @Bean
    public ServerLogoutSuccessHandler azureLogoutSuccessHandler() {
        return (exchange, authentication) -> {
            ServerWebExchange webExchange = exchange.getExchange();
            webExchange.getResponse().setStatusCode(HttpStatus.FOUND);
            webExchange.getResponse().getHeaders().setLocation(URI.create(logoutURI));
            return webExchange.getResponse().setComplete();
        };
    }
}