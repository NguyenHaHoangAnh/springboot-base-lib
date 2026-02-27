package com.example.lib.auth.config;

import com.example.lib.auth.filter.JwtTokenFilter;
import com.example.lib.auth.service.TokenService;
import com.example.lib.auth.util.AuthUtils;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(AuthUtils.class)
@AllArgsConstructor
public class AuthConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate() { return new RestTemplate(); }

    @Bean
    public TokenService tokenService(AuthUtils authUtils, RestTemplate restTemplate) {
        return new TokenService(authUtils, restTemplate);
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter(TokenService tokenService) {
        return new JwtTokenFilter(tokenService);
    }

}
