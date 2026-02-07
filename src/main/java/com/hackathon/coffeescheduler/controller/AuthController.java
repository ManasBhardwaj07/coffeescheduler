package com.hackathon.coffeescheduler.controller;

import com.hackathon.coffeescheduler.model.User;
import com.hackathon.coffeescheduler.model.dto.LoginRequest;
import com.hackathon.coffeescheduler.model.dto.SignupRequest;
import com.hackathon.coffeescheduler.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;   // ✅ REQUIRED IMPORT

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService auth;

    @PostMapping("/signup")
    public Map<String,Object> signup(@RequestBody SignupRequest req) {

        User u = auth.signup(req.getEmail(), req.getPassword());

        return Map.of(
                "id", u.getId(),
                "email", u.getEmail()
        );
    }

    @PostMapping("/login")
    public Map<String,Object> login(@RequestBody LoginRequest req) {
        return auth.login(req.getEmail(), req.getPassword());
    }
}
