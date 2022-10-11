package com.srs.account.grpc.service;


import com.market.account.GetCurrentUserResponse;
import com.market.account.GetUserResponse;
import com.market.account.ListUsersRequest;
import com.market.account.UpsertUserRequest;
import com.market.common.FindByCodeRequest;
import com.market.common.ListResponse;
import com.market.common.NoContentResponse;
import com.market.common.PageResponse;
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
