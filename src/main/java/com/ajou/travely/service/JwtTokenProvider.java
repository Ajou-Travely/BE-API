package com.ajou.travely.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    @Value("${auth.jwtSecretKey}")
    private String secretKey;
    @Value("${auth.tokenValidTime}")
    private Long tokenValidTime;

    private final CustomUserDetailsService customUserDetailsService;

    @PostConstruct
    public void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(Long userId, String accessToken) {
        Date now = new Date();
        Date expiredAt = new Date(now.getTime() + tokenValidTime);

        return Jwts.builder()
                .claim("userId", userId)
                .claim("accessToken", accessToken)
                .setIssuedAt(now)
                .setExpiration(expiredAt)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) throws ParseException {
        UserDetails userDetails = customUserDetailsService.loadUserByUserId(this.getUserId(token), this.getAccessToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public Long getUserId(String token) throws ParseException {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                .getBody().get("userId", Long.class);
    }

    public String getAccessToken(String token) throws ParseException {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                .getBody().get("accessToken", String.class);
    }

    public String resolveToken(HttpServletRequest request) {
        String authentication = request.getHeader("Authentication");
        if (Objects.nonNull(authentication)) {
            String[] result = authentication.split("Bearer ");
            return result[1];
        }
        return null;

    }

    public Boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey)
                    .parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
