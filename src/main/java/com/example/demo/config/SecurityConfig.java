package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                    // allow preflight CORS requests
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    // open auth APIs
                    .requestMatchers("/api/auth/**").permitAll()
                    // protect profile APIs
                    .requestMatchers("/api/profile/**").authenticated()
                    // any other request requires authentication
                    .anyRequest().authenticated()
            )
            .cors(); // enable CORS

        return http.build();
    }

    // Global CORS configuration
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://ec2-34-228-81-125.compute-1.amazonaws.com") // allow only your EC2
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true); // set to true when specifying a specific origin
            }
        };
    }
}
