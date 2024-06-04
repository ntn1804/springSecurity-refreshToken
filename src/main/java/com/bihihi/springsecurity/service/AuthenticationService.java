package com.bihihi.springsecurity.service;

import com.bihihi.springsecurity.dto.JwtAuthenticationResponse;
import com.bihihi.springsecurity.dto.RefreshTokenRequest;
import com.bihihi.springsecurity.dto.SignUpRequest;
import com.bihihi.springsecurity.dto.SigninRequest;
import com.bihihi.springsecurity.model.User;

public interface AuthenticationService {

    User signup(SignUpRequest signUpRequest);

    JwtAuthenticationResponse signin(SigninRequest signinRequest);

    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
