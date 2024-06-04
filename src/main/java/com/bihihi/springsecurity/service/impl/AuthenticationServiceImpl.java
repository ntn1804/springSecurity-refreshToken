package com.bihihi.springsecurity.service.impl;

import com.bihihi.springsecurity.dto.JwtAuthenticationResponse;
import com.bihihi.springsecurity.dto.RefreshTokenRequest;
import com.bihihi.springsecurity.dto.SignUpRequest;
import com.bihihi.springsecurity.dto.SigninRequest;
import com.bihihi.springsecurity.model.Role;
import com.bihihi.springsecurity.model.User;
import com.bihihi.springsecurity.repository.UserRepository;
import com.bihihi.springsecurity.service.AuthenticationService;
import com.bihihi.springsecurity.service.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JWTService jwtService;

    public User signup(SignUpRequest signUpRequest) {
        User user = new User();

        user.setFirstname(signUpRequest.getFirstName());
        user.setLastname(signUpRequest.getLastName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole(Role.USER);

        return userRepository.save(user);
    }

    public JwtAuthenticationResponse signin(SigninRequest signinRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getEmail(),
                signinRequest.getPassword()));

        var user = userRepository.findByEmail(signinRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);

        return jwtAuthenticationResponse;
    }

    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String userEmail = jwtService.extractUsername(refreshTokenRequest.getToken());
        User user = userRepository.findByEmail(userEmail).orElseThrow();

        if (jwtService.isTokenValid(refreshTokenRequest.getToken(), user)) {
            var jwt = jwtService.generateToken(user);

            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());

            return jwtAuthenticationResponse;
        }

        return null;
    }
}
