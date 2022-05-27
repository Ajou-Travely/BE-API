package com.ajou.travely.config.auth;

import com.ajou.travely.service.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authorizeRequests()
                        .antMatchers("/", "/v1/oauth2/authorization/kakao", "/v1/users/signup", "/v1/login", "/v1/users/email").permitAll()
                        .antMatchers("/swagger*/**", "/v3/api-docs/**").permitAll()
                        .antMatchers("/v1/**").hasAnyRole("USER", "ADMIN")
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class);
//                .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')");
//                .antMatchers("/api/v2/**").access("hasRole('ROLE_ADMIN')")
//                .antMatchers("/**").permitAll();
    }
}
