package com.srs.account.grpc.service;

import com.market.common.FindByIdRequest;
import com.market.common.FindByIdsRequest;
import com.market.common.NoContentResponse;
import com.market.common.PageResponse;
import com.srs.account.GetRoleResponse;
import com.srs.account.ListRoleRequest;
import com.srs.account.UpsertRoleRequest;
import com.srs.proto.dto.GrpcPrincipal;

public interface RoleGrpcService {
    PageResponse listRoles(ListRoleRequest request, GrpcPrincipal principal);

    GetRoleResponse getRole(FindByIdRequest request, GrpcPrincipal principal);

    NoContentResponse createRole(UpsertRoleRequest request, GrpcPrincipal identity);

    NoContentResponse updateRole(UpsertRoleRequest request, GrpcPrincipal identity);

    NoContentResponse deleteRole(FindByIdRequest request, GrpcPrincipal principal);

    PageResponse findAllByIds(FindByIdsRequest request, GrpcPrincipal principal);
}
