package com.srs.account.grpc.service;

import com.srs.account.LoginRequest;
import com.srs.account.LoginResponse;

public interface AuthGrpcService {
    LoginResponse login(LoginRequest request);
}
