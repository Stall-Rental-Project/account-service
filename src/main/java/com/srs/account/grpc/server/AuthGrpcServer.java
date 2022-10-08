package com.srs.account.grpc.server;

import com.market.account.AuthServiceGrpc;
import com.market.account.LoginRequest;
import com.market.account.LoginResponse;
import com.srs.account.grpc.service.AuthGrpcService;
import com.srs.proto.util.GrpcExceptionUtil;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
@Log4j2
public class AuthGrpcServer extends AuthServiceGrpc.AuthServiceImplBase {
    private final AuthGrpcService authGrpcService;

    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        try {
            log.info("Password is {}", request.getPassword());
            responseObserver.onNext(authGrpcService.login(request));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onNext(LoginResponse.newBuilder()
                    .setSuccess(false)
                    .setError(GrpcExceptionUtil.asGrpcError(e))
                    .build());
            responseObserver.onCompleted();
            throw e;
        }
    }
}
