package com.srs.account.grpc.server;

import com.srs.common.FindByIdRequest;
import com.srs.common.FindByIdsRequest;
import com.srs.common.NoContentResponse;
import com.srs.common.PageResponse;
import com.srs.account.GetRoleResponse;
import com.srs.account.ListRoleRequest;
import com.srs.account.RoleServiceGrpc;
import com.srs.account.UpsertRoleRequest;
import com.srs.account.grpc.service.RoleGrpcService;
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
public class RoleGrpcServer extends RoleServiceGrpc.RoleServiceImplBase {
    private final RoleGrpcService roleGrpcService;

    @Override
    public void listRoles(ListRoleRequest request, StreamObserver<PageResponse> responseObserver) {
        try {
            var principal = GrpcPrincipalProvider.getGrpcPrincipal();
            responseObserver.onNext(roleGrpcService.listRoles(request, principal));
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
    public void getRole(FindByIdRequest request, StreamObserver<GetRoleResponse> responseObserver) {
        try {
            var principal = GrpcPrincipalProvider.getGrpcPrincipal();
            responseObserver.onNext(roleGrpcService.getRole(request, principal));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onNext(GetRoleResponse.newBuilder()
                    .setSuccess(false)
                    .setError(GrpcExceptionUtil.asGrpcError(e))
                    .build());
            responseObserver.onCompleted();
            throw e;
        }
    }

    @Override
    public void createRole(UpsertRoleRequest request, StreamObserver<NoContentResponse> responseObserver) {
        try {
            var principal = GrpcPrincipalProvider.getGrpcPrincipal();
            responseObserver.onNext(roleGrpcService.createRole(request, principal));
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
    public void updateRole(UpsertRoleRequest request, StreamObserver<NoContentResponse> responseObserver) {
        try {
            var principal = GrpcPrincipalProvider.getGrpcPrincipal();
            responseObserver.onNext(roleGrpcService.updateRole(request, principal));
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
    public void deleteRole(FindByIdRequest request, StreamObserver<NoContentResponse> responseObserver) {
        try {
            var principal = GrpcPrincipalProvider.getGrpcPrincipal();
            responseObserver.onNext(roleGrpcService.deleteRole(request, principal));
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
    public void findAllByIds(FindByIdsRequest request, StreamObserver<PageResponse> responseObserver) {
        try {
            var principal = GrpcPrincipalProvider.getGrpcPrincipal();
            responseObserver.onNext(roleGrpcService.findAllByIds(request, principal));
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
}
