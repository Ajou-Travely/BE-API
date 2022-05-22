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

    public String createToken(Long userId) {
        Map<String, Long> userInfo = new HashMap<>();
        userInfo.put("userId", userId);
        JSONObject payload = new JSONObject(userInfo);
        Claims claims = Jwts.claims().setSubject(payload.toJSONString());
        Date now = new Date();
        Date expiredAt = new Date(now.getTime() + tokenValidTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredAt)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) throws ParseException {
        UserDetails userDetails = customUserDetailsService.loadUserByUserId(this.getUserId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public Long getUserId(String token) throws ParseException {
        String subject = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                .getBody().getSubject();
        JSONParser jsonParser = new JSONParser();
        JSONObject parse = (JSONObject) jsonParser.parse(subject);

        return (Long) parse.get("userId");
    }

    public String resolveToken(HttpServletRequest request) {
        String authentication = request.getHeader("Authentication");
        if (Objects.nonNull(authentication)) {
            String[] result = authentication.split("Bearer ");
            return result[0];
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
