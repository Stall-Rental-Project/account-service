package com.srs.account.grpc.service;


import com.market.account.UpsertUserRequest;
import com.market.common.NoContentResponse;
import com.srs.proto.dto.GrpcPrincipal;

public interface UserGrpcService {
    NoContentResponse createUser(UpsertUserRequest request, GrpcPrincipal principal) throws Exception;
}
