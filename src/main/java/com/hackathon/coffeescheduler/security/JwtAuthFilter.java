package com.hackathon.coffeescheduler.security;

import com.hackathon.coffeescheduler.security.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JwtAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest req,
            HttpServletResponse res,
            FilterChain chain
    ) throws ServletException, IOException {

        String path = req.getRequestURI();

        // ✅ skip public endpoints
        if (path.startsWith("/auth") || path.startsWith("/h2-console")) {
            chain.doFilter(req, res);
            return;
        }

        try {
            String header = req.getHeader("Authorization");

            if (header != null && header.startsWith("Bearer ")) {

                String token = header.substring(7).trim();

                Claims claims = JwtUtil.parse(token).getBody();

                // only set auth if not already set
                if (SecurityContextHolder.getContext().getAuthentication() == null) {

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    claims.getSubject(),
                                    null,
                                    Collections.emptyList()
                            );

                    SecurityContextHolder.getContext().setAuthentication(auth);

                    // optional extra data
                    req.setAttribute("uid", claims.get("uid"));
                }
            }

        } catch (Exception e) {
            // ✅ DO NOT block request here — just log
            System.out.println("JWT FILTER ERROR: " + e.getMessage());
        }

        // ✅ always continue filter chain
        chain.doFilter(req, res);
    }
}
