package com.reaction.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * JWT工具类
 */
@Component
public class JwtUtil {
    
    @Value("${jwt.secret:reaction-path-visualization-secret-key-2024}")
    private String secret;
    
    @Value("${jwt.expiration:86400000}")
    private Long expiration;
    
    /**
     * 生成JWT token
     */
    public String generateToken(Long userId, String username) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withClaim("userId", userId)
                    .withClaim("username", username)
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Failed to create JWT token", exception);
        }
    }
    
    /**
     * 验证JWT token
     */
    public boolean verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWT.require(algorithm)
                    .build()
                    .verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            return false;
        }
    }
    
    /**
     * 从token中获取userId
     */
    public Long getUserIdFromToken(String token) {
        try {
            return JWT.decode(token).getClaim("userId").asLong();
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 从token中获取username
     */
    public String getUsernameFromToken(String token) {
        try {
            return JWT.decode(token).getClaim("username").asString();
        } catch (Exception e) {
            return null;
        }
    }
}
