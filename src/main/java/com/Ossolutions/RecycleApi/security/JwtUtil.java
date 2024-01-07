package com.Ossolutions.RecycleApi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;
@Component

public class JwtUtil {
    private final Key secretKey;

    @Value("${jwt.expirationMs}")
    private long expirationMs;

    public JwtUtil() {
        // Use Keys.secretKeyFor to generate a secure key
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public String generateToken(String username) {
        long expirationTime = System.currentTimeMillis() + expirationMs;

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(expirationTime))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract the expiration date from a JWT
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract a specific claim from a JWT using a claims resolver function
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract all claims from a JWT
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    // Check if a JWT is expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Validate a JWT against a UserDetails object
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
