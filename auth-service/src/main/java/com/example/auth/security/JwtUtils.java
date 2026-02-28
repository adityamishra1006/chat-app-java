package com.example.auth.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Component
@Slf4j
public class JwtUtils {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access-expiration}")
    private long accessTokenExpiration;

    private SecretKey key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(UserPrincipal userPrincipal) {

        return Jwts.builder()
                .setSubject(userPrincipal.getId().toString())
                .claim("username", userPrincipal.getUsername())
                .claim("role", userPrincipal.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token){
        try{
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e){
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    public UUID getUserIdFromToken(String token){
        String subject = getClaims(token).getSubject();
        return UUID.fromString(subject);
    }

    public String getUsernameFromToken(String token){
        return getClaims(token).get("username", String.class);
    }

    private Claims getClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
