package com.cabgo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class CabGoApplication {
    public static void main(String[] args) { SpringApplication.run(CabGoApplication.class, args); }

    @Bean
    WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**").allowedOrigins("http://localhost:5173")
                    .allowedMethods("GET","POST","PATCH","PUT","DELETE","OPTIONS").allowedHeaders("*");
            }
        };
    }
}
