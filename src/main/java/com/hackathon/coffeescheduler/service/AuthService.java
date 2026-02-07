package com.hackathon.coffeescheduler.service;

import com.hackathon.coffeescheduler.model.User;
import com.hackathon.coffeescheduler.repository.UserRepository;
import com.hackathon.coffeescheduler.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public User signup(String email, String rawPassword) {

        if (email == null || rawPassword == null || rawPassword.isBlank()) {
            throw new RuntimeException("email/password required");
        }

        if (repo.findByEmail(email).isPresent()) {
            throw new RuntimeException("email exists");
        }

        User u = User.builder()
                .email(email)
                .password(encoder.encode(rawPassword))
                .build();

        return repo.save(u);
    }

    public Map<String,Object> login(String email, String raw) {

        if (raw == null) throw new RuntimeException("password required");

        User u = repo.findByEmail(email).orElseThrow();

        if (!encoder.matches(raw, u.getPassword()))
            throw new RuntimeException("bad creds");

        String token = JwtUtil.generate(u.getEmail(), u.getId());

        return Map.of(
                "token", token,
                "userId", u.getId(),
                "email", u.getEmail()
        );
    }
}
