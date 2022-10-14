package com.srs.account.grpc.service;

import com.srs.common.FindByIdRequest;
import com.srs.common.FindByIdsRequest;
import com.srs.common.NoContentResponse;
import com.srs.common.PageResponse;
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
