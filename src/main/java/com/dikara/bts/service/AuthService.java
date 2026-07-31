package com.dikara.bts.service;

import com.dikara.bts.dto.request.LoginRequest;
import com.dikara.bts.dto.request.RegisterRequest;
import com.dikara.bts.dto.response.LoginResponse;

public interface AuthService {

    void register(RegisterRequest request);

    LoginResponse login(LoginRequest request);
}
