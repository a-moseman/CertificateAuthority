package org.amoseman.certificateauthority.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("securityFilterChain invoked"); // DEBUG
        http
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll()) // was .authenticated()
                .x509(Customizer.withDefaults())
                .csrf((csrf) -> csrf.disable())
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
