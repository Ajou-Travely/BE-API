package com.ajou.travely.config.auth;

import com.ajou.travely.domain.user.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/v1/oauth2/authorization/kakao", "/v1/isLogin", "/v1/signup").permitAll()
                .antMatchers("/swagger*/**", "/v3/api-docs/**").permitAll()
                .antMatchers("/v1/**").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')");
//                .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')");
//                .antMatchers("/api/v2/**").access("hasRole('ROLE_ADMIN')")
    }
}
