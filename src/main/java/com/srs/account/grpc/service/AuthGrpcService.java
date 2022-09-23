package com.srs.account.grpc.service;

import com.market.account.LoginRequest;
import com.market.account.LoginResponse;

public interface AuthGrpcService {
    LoginResponse login(LoginRequest request);
}
