package com.ajou.travely.config;

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
                .antMatchers("/api/v1/oauth2/authorization/kakao", "/api/v1/isLogin", "/api/v1/signup").permitAll()
                .antMatchers("/api/v1/**").permitAll();//.access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')");
//                .antMatchers("/api/v2/**").access("hasRole('ROLE_ADMIN')")
    }
}
