package com.example.lib.auth.config;

import com.example.lib.auth.filter.JwtTokenFilter;
import com.example.lib.auth.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfiguration {
    private final AuthUtils authUtils;
    private final JwtTokenFilter jwtTokenFilter;
    private static final String[] AUTH_WHITELIST = {
            // -- Swagger UI v2
            "/api/*/*/v2/api-docs",
            "/api/*/*/swagger-resources",
            "/api/*/*/swagger-resources/**",
            // -- Swagger UI v3 (OpenAPI)
            "/api/*/*/v3/api-docs/**",
            "/api/*/*/swagger-ui/**",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // enable cors
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // disable csrf
                .csrf(csrf -> csrf.disable())
                // stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // authorize
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(authUtils.getPermitUrls()).permitAll()
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .anyRequest().authenticated()
                )
                // add jwtToken filter
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
                // add basicAuth filter
                // .addFilterBefore(basicAuthFilter, UsernamePasswordAuthenticationFilter.class);
                // add cookie filter
                // .addFilterBefore(cookieFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Cấu hình CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(false); // nếu dùng cookie thì phải true

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
