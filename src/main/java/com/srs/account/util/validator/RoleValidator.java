package com.srs.account.util.validator;

import com.srs.common.NoContentResponse;
import com.srs.account.UpsertRoleRequest;
import com.srs.proto.dto.GrpcPrincipal;

public interface RoleValidator {
    NoContentResponse validateCreateRoleRequest(UpsertRoleRequest request);

    NoContentResponse validateUpdateRoleRequest(UpsertRoleRequest request, GrpcPrincipal identity);
}
