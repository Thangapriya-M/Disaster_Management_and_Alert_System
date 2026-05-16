package com.dmas.controller;

import com.dmas.model.User;
import com.dmas.service.AuthService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")

@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, Object> body) {
        try {
            String name     = (String) body.get("name");
            String email    = (String) body.get("email");
            String password = (String) body.get("password");
            String phone    = (String) body.getOrDefault("phone", "");
            String roleStr  = (String) body.getOrDefault("role", "CITIZEN");
            User.Role role  = User.Role.valueOf(roleStr.toUpperCase());
            Double lat = body.get("latitude")  != null ? Double.parseDouble(body.get("latitude").toString())  : null;
            Double lon = body.get("longitude") != null ? Double.parseDouble(body.get("longitude").toString()) : null;

            Map<String, Object> result = authService.register(name, email, password, phone, role, lat, lon);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        try {
            Map<String, Object> result = authService.login(body.get("email"), body.get("password"));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid email or password"));
        }
    }
}
