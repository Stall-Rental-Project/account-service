package com.srs.account.grpc.server;

import com.market.account.ListUsersRequest;
import com.market.account.UpsertUserRequest;
import com.market.account.UserServiceGrpc;
import com.market.common.NoContentResponse;
import com.market.common.PageResponse;
import com.srs.account.grpc.service.UserGrpcService;
import com.srs.proto.provider.GrpcPrincipalProvider;
import com.srs.proto.util.GrpcExceptionUtil;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
@Log4j2
public class UserGrpcServer extends UserServiceGrpc.UserServiceImplBase {
    private final UserGrpcService userGrpcService;

    @Override
    public void listUsers(ListUsersRequest request, StreamObserver<PageResponse> responseObserver) {
        super.listUsers(request, responseObserver);
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
}
