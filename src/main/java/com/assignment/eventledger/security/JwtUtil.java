package com.assignment.eventledger.security;

import io.jsonwebtoken.Jwts;
import java.util.Date;

public class JwtUtil {
    public static String generateToken(String username) {

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(JwtKey.SECRET_KEY)
                .compact();
    }
}
