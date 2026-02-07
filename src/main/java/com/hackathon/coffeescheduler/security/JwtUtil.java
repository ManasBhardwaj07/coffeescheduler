package com.hackathon.coffeescheduler.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {

    private static final Key key =
            Keys.hmacShaKeyFor("hackathon-secret-key-hackathon-secret-key".getBytes());

    private static final long EXP = 1000 * 60 * 60 * 6; // 6h

    public static String generate(String email, Long uid) {
        return Jwts.builder()
                .setSubject(email)
                .claim("uid", uid)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXP))
                .signWith(key)
                .compact();
    }

    public static Jws<Claims> parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }
}
