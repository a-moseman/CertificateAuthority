package org.amoseman.certificateauthority.application;

import org.amoseman.certificateauthority.auth.UserBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        //.anyRequest().authenticated()
                        .anyRequest().permitAll()
                )
                .x509(x509 -> x509
                        .subjectPrincipalRegex("CN=(.*?)(?:,|$)")
                        .userDetailsService(userDetailsService())
                )
                .csrf(AbstractHttpConfigurer::disable);
                //.httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Autowired
    @Qualifier("adminName")
    private String adminName;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            UserBuilder builder = new UserBuilder()
                    .setUsername(username)
                    .setIsExpired(false);
            if (adminName.equals(username)) {
                return builder.setRoles("ADMIN").build();
            }
            return builder.setRoles("GUEST").build();
        };
    }
}
