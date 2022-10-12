package com.srs.account.grpc.server;

import com.google.protobuf.Empty;
import com.market.account.*;
import com.market.common.FindByCodeRequest;
import com.market.common.ListResponse;
import com.market.common.NoContentResponse;
import com.market.common.PageResponse;
import com.srs.account.grpc.service.UserGrpcService;
import com.srs.proto.intercepter.AuthGrpcInterceptor;
import com.srs.proto.provider.GrpcPrincipalProvider;
import com.srs.proto.util.GrpcExceptionUtil;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService(interceptors = AuthGrpcInterceptor.class)
@RequiredArgsConstructor
@Log4j2
public class UserGrpcServer extends UserServiceGrpc.UserServiceImplBase {
    private final UserGrpcService userGrpcService;

    @Override
    public void listUsers(ListUsersRequest request, StreamObserver<PageResponse> responseObserver) {
        try {
            var principal = GrpcPrincipalProvider.getGrpcPrincipal();
            responseObserver.onNext(userGrpcService.listUsers(request, principal));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onNext(PageResponse.newBuilder()
                    .setSuccess(false)
                    .setError(GrpcExceptionUtil.asGrpcError(e))
                    .build());
            responseObserver.onCompleted();
            throw e;
        }
    }

    @Override
    public void createUser(UpsertUserRequest request, StreamObserver<NoContentResponse> responseObserver) {
        try {
            var principal = GrpcPrincipalProvider.getGrpcPrincipal();
            responseObserver.onNext(userGrpcService.createUser(request, principal));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onNext(NoContentResponse.newBuilder()
                    .setSuccess(false)
                    .setError(GrpcExceptionUtil.asGrpcError(e))
                    .build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void updateUser(UpsertUserRequest request, StreamObserver<NoContentResponse> responseObserver) {
        try {
            var principal = GrpcPrincipalProvider.getGrpcPrincipal();
            responseObserver.onNext(userGrpcService.updateUser(request, principal));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onNext(NoContentResponse.newBuilder()
                    .setSuccess(false)
                    .setError(GrpcExceptionUtil.asGrpcError(e))
                    .build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void listUsersByEmail(FindByCodeRequest request, StreamObserver<ListResponse> responseObserver) {
        try {
            var principal = GrpcPrincipalProvider.getGrpcPrincipal();
            responseObserver.onNext(userGrpcService.listUsersByEmail(request, principal));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onNext(ListResponse.newBuilder()
                    .setSuccess(false)
                    .setError(GrpcExceptionUtil.asGrpcError(e))
                    .build());
            responseObserver.onCompleted();
            throw e;
        }
    }

    @Override
    public void getUser(FindByCodeRequest request, StreamObserver<GetUserResponse> responseObserver) {
        try {
            var principal = GrpcPrincipalProvider.getGrpcPrincipal();
            responseObserver.onNext(userGrpcService.getUser(request, principal));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onNext(GetUserResponse.newBuilder()
                    .setSuccess(false)
                    .setError(GrpcExceptionUtil.asGrpcError(e))
                    .build());
            responseObserver.onCompleted();
            throw e;
        }
    }

    @Override
    public void deleteUser(FindByCodeRequest request, StreamObserver<NoContentResponse> responseObserver) {
        try {
            var principal = GrpcPrincipalProvider.getGrpcPrincipal();
            responseObserver.onNext(userGrpcService.deleteUser(request, principal));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onNext(NoContentResponse.newBuilder()
                    .setSuccess(false)
                    .setError(GrpcExceptionUtil.asGrpcError(e))
                    .build());
            responseObserver.onCompleted();
            throw e;
        }
    }

    @Override
    public void getCurrentUser(Empty request, StreamObserver<GetCurrentUserResponse> responseObserver) {
        try {
            var principal = GrpcPrincipalProvider.getGrpcPrincipal();
            responseObserver.onNext(userGrpcService.getCurrentUser(principal));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onNext(GetCurrentUserResponse.newBuilder()
                    .setSuccess(false)
                    .setError(GrpcExceptionUtil.asGrpcError(e))
                    .build());
            responseObserver.onCompleted();
            throw e;
        }
    }
}
