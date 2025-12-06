package com.example.recipemanagement.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(request -> request
                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml"
                        ).permitAll()
                        .requestMatchers("/api/category/add").hasAnyRole("ADMIN","CHEF")
                        .requestMatchers("/api/category/update/{categoryId}").hasAnyRole("ADMIN","CHEF")
                        .requestMatchers("/api/recipes/add").hasAnyRole("ADMIN","CHEF")
                        .requestMatchers("/api/recipes/update/{recipeId}").hasAnyRole("ADMIN","CHEF")
                        .requestMatchers("/api/recipes/delete/{recipeId}").hasAnyRole("ADMIN","CHEF")
                        .requestMatchers("/api/recipes/all").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/api/recipes/home").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/api/recipes/search").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/api/recipes/likes/{recipeId}").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/api/recipes/unlikes/{recipeId}").hasAnyRole("USER","ADMIN")
                       // .requestMatchers("/api/recipes/likes/all/{recipeId}").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/api/recipes/comments/{recipeId}").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/api/recipes/comments/{commentId}").hasAnyRole("USER","ADMIN")
                        //.requestMatchers("/api/recipes/all/{recipeId}/comments").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/admin/audit/all").hasAnyRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}




