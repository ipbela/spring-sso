package com.example.Gateway;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
@EnableWebFlux
public class WebConfig implements WebFluxConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173") // URL do seu aplicativo React
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Permitir os métodos necessários
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
