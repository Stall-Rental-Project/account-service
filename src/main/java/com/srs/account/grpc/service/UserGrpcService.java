package com.srs.account.grpc.service;


import com.srs.account.GetCurrentUserResponse;
import com.srs.account.GetUserResponse;
import com.srs.account.ListUsersRequest;
import com.srs.account.UpsertUserRequest;
import com.srs.common.FindByCodeRequest;
import com.srs.common.ListResponse;
import com.srs.common.NoContentResponse;
import com.srs.common.PageResponse;
import com.srs.proto.dto.GrpcPrincipal;

public interface UserGrpcService {
    NoContentResponse createUser(UpsertUserRequest request, GrpcPrincipal principal) throws Exception;

    NoContentResponse updateUser(UpsertUserRequest request, GrpcPrincipal principal) throws Exception;

    GetUserResponse getUser(FindByCodeRequest request, GrpcPrincipal principal);
    GetCurrentUserResponse getCurrentUser(GrpcPrincipal principal);

    ListResponse listUsersByEmail(FindByCodeRequest request, GrpcPrincipal principal);

    PageResponse listUsers(ListUsersRequest request, GrpcPrincipal principal);

    NoContentResponse deleteUser(FindByCodeRequest request, GrpcPrincipal principal);

}
