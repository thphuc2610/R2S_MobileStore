package com.example.mobile_store.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.mobile_store.constant.ApiUrlConstant;
import com.example.mobile_store.repository.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private UserRepository userRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
            AuthFilter authFilter) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        httpSecurity.sessionManagement(configure -> configure.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

        httpSecurity.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                .requestMatchers(ApiUrlConstant.USER + "/**").permitAll()

                .requestMatchers(HttpMethod.GET, ApiUrlConstant.CATEGORY + "/**").permitAll()
                .requestMatchers(HttpMethod.POST, ApiUrlConstant.CATEGORY + "/**").hasAuthority("ROLE_ADMIN") 
                .requestMatchers(HttpMethod.PUT, ApiUrlConstant.CATEGORY + "/**").hasAuthority("ROLE_ADMIN") 
                .requestMatchers(HttpMethod.DELETE, ApiUrlConstant.CATEGORY + "/**").hasAuthority("ROLE_ADMIN") 

                .requestMatchers(HttpMethod.GET, ApiUrlConstant.PRODUCT + "/**").permitAll()
                .requestMatchers(HttpMethod.POST, ApiUrlConstant.PRODUCT + "/**").hasAuthority("ROLE_ADMIN") 
                .requestMatchers(HttpMethod.PUT, ApiUrlConstant.PRODUCT + "/**").hasAuthority("ROLE_ADMIN") 
                .requestMatchers(HttpMethod.DELETE, ApiUrlConstant.PRODUCT + "/**").hasAuthority("ROLE_ADMIN") 

                .requestMatchers(ApiUrlConstant.IMAGE + "/**").permitAll()
                .requestMatchers(ApiUrlConstant.UPLOAD + "/**").permitAll()
                .requestMatchers(ApiUrlConstant.ORDER + "/**").permitAll()
                .requestMatchers(ApiUrlConstant.ORDER_DETAIL + "/**").permitAll()
                
                .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found" +
                        username));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}