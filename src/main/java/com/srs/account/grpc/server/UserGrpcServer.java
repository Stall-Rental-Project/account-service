package com.srs.account.grpc.server;

import com.market.account.*;
import com.market.common.*;
import com.srs.account.grpc.service.UserGrpcService;
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
    public void listUsersByExternalIds(FindByCodesRequest request, StreamObserver<ListResponse> responseObserver) {
        super.listUsersByExternalIds(request, responseObserver);
    }

    @Override
    public void listUsersByEmail(FindByCodeRequest request, StreamObserver<ListResponse> responseObserver) {
        super.listUsersByEmail(request, responseObserver);
    }

    @Override
    public void listUserRecipients(ListUserRecipientsRequest request, StreamObserver<PageResponse> responseObserver) {
        super.listUserRecipients(request, responseObserver);
    }

    @Override
    public void getUser(FindByCodeRequest request, StreamObserver<GetUserResponse> responseObserver) {
        super.getUser(request, responseObserver);
    }

    @Override
    public void createUser(UpsertUserRequest request, StreamObserver<NoContentResponse> responseObserver) {
        super.createUser(request, responseObserver);
    }

    @Override
    public void updateUser(UpsertUserRequest request, StreamObserver<NoContentResponse> responseObserver) {
        super.updateUser(request, responseObserver);
    }

    @Override
    public void deleteUser(FindByCodeRequest request, StreamObserver<NoContentResponse> responseObserver) {
        super.deleteUser(request, responseObserver);
    }
}
