package com.srs.account.grpc.server;

import com.market.common.FindByIdRequest;
import com.market.common.ListResponse;
import com.srs.account.ListPermissionByUserResponse;
import com.srs.account.ListPermissionCategoryRequest;
import com.srs.account.ListPermissionsRequest;
import com.srs.account.PermissionServiceGrpc;
import com.srs.account.grpc.service.PermissionGrpcService;
import com.srs.proto.intercepter.AuthGrpcInterceptor;
import com.srs.proto.util.GrpcExceptionUtil;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService(interceptors = AuthGrpcInterceptor.class)
@RequiredArgsConstructor
@Log4j2
public class PermissionGrpcServer extends PermissionServiceGrpc.PermissionServiceImplBase {
    private final PermissionGrpcService permissionGrpcService;

    @Override
    public void listPermissionsByUser(FindByIdRequest request, StreamObserver<ListPermissionByUserResponse> responseObserver) {
        try {
            responseObserver.onNext(permissionGrpcService.listPermissionsByUser(request));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onNext(ListPermissionByUserResponse.newBuilder()
                    .setSuccess(false)
                    .setError(GrpcExceptionUtil.asGrpcError(e))
                    .build());
            responseObserver.onCompleted();
            throw e;
        }
    }

    @Override
    public void listPermissions(ListPermissionsRequest request, StreamObserver<ListResponse> responseObserver) {
        try {
            responseObserver.onNext(permissionGrpcService.listPermissions(request));
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
    public void listPermissionCategories(ListPermissionCategoryRequest request, StreamObserver<ListResponse> responseObserver) {
        try {
            responseObserver.onNext(permissionGrpcService.listPermissionCategories(request));
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
}
