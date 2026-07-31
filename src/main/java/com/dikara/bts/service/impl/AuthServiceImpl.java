package com.dikara.bts.service.impl;

import com.dikara.bts.dto.request.LoginRequest;
import com.dikara.bts.dto.request.RegisterRequest;
import com.dikara.bts.dto.response.LoginResponse;
import com.dikara.bts.entity.User;
import com.dikara.bts.exception.DuplicateResourceException;
import com.dikara.bts.repository.UserRepository;
import com.dikara.bts.service.AuthService;
import com.dikara.bts.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final CustomUserDetailServiceImpl userDetailsService;

    private final JwtService jwtService;



    @Override
    public void register(RegisterRequest request) {

        if (userRepository.findByUsername(
                request.getUsername()).isPresent()) {

            throw new DuplicateResourceException(
                    "Username already exists"
            );
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(
                        passwordEncoder.encode(
                                request.getPassword()
                        )
                )
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

    }

    @Override
    public LoginResponse login(
            LoginRequest request
    ) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        UserDetails userDetails =
                userDetailsService.loadUserByUsername(
                                request.getUsername()
                        );

        String accessToken =
                jwtService.generateToken(
                        userDetails
                );

        String refreshToken =
                jwtService.generateRefreshToken(
                        userDetails
                );

        return LoginResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .username(userDetails.getUsername())
                .build();
    }
}
