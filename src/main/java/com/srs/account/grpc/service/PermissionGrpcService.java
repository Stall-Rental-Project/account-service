package com.srs.account.grpc.service;

import com.srs.common.FindByIdRequest;
import com.srs.common.ListResponse;
import com.srs.account.ListPermissionByUserResponse;
import com.srs.account.ListPermissionCategoryRequest;
import com.srs.account.ListPermissionsRequest;

public interface PermissionGrpcService {
    ListPermissionByUserResponse listPermissionsByUser(FindByIdRequest request);

    ListResponse listPermissions(ListPermissionsRequest request);

    ListResponse listPermissionCategories(ListPermissionCategoryRequest request);
}
