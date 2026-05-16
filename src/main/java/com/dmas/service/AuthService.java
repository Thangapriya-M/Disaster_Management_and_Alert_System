package com.dmas.service;

import com.dmas.model.User;
import com.dmas.repository.UserRepository;
import com.dmas.security.JwtUtils;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    public Map<String, Object> register(String name, String email, String password,
                                         String phone, User.Role role,
                                         Double latitude, Double longitude) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already registered");
        }
        User user = User.builder()
                .name(name).email(email)
                .password(passwordEncoder.encode(password))
                .phone(phone).role(role)
                .latitude(latitude).longitude(longitude)
                .build();
        userRepository.save(user);
        String token = jwtUtils.generateToken(email, role.name());
        return Map.of("token", token, "name", name, "email", email,
                      "role", role.name(), "userId", user.getId());
    }

    public Map<String, Object> login(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        String token = jwtUtils.generateToken(email, user.getRole().name());
        return Map.of("token", token, "name", user.getName(), "email", email,
                      "role", user.getRole().name(), "userId", user.getId());
    }
}
