package com.billing.calculator.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	private void configure(HttpSecurity http) throws Exception {
        http.csrf().disable() // CSRF korumasını devre dışı bırak
            .authorizeRequests()
            .requestMatchers("/**").permitAll(); 
    }
}