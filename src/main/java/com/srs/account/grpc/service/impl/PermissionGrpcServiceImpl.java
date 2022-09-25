package com.srs.account.grpc.service.impl;

import com.market.common.FindByIdRequest;
import com.market.common.ListResponse;
import com.srs.account.*;
import com.srs.account.grpc.service.PermissionGrpcService;
import com.srs.account.repository.PermissionDslRepository;
import com.srs.account.repository.RoleDslRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Log4j2
public class PermissionGrpcServiceImpl implements PermissionGrpcService {

    private final PermissionDslRepository permissionDslRepository;
    private final RoleDslRepository roleDslRepository;


    @Override
    public ListPermissionByUserResponse listPermissionsByUser(FindByIdRequest request) {
        var userId = UUID.fromString(request.getId());

        var permissions = permissionDslRepository.findAllByUserId(userId);

        var roleIds = roleDslRepository.findAllIdsByUserId(userId).stream()
                .map(UUID::toString)
                .collect(Collectors.toList());

        var grpcPermissions = new ArrayList<Permission>();

        for (var tuple : permissions) {
            var permissionId = tuple.get(0, UUID.class);
            var code = tuple.get(1, String.class);
            var name = tuple.get(2, String.class);
            var category = tuple.get(3, String.class);

            assert permissionId != null;
            var grpcPermission = Permission.newBuilder()
                    .setPermissionId(permissionId.toString())
                    .setCode(code)
                    .setName(name)
                    .setPermissionCategory(category)
                    .build();

            grpcPermissions.add(grpcPermission);
        }

        return ListPermissionByUserResponse.newBuilder()
                .setSuccess(true)
                .setData(ListPermissionByUserResponse.Data.newBuilder()
                        .setRolePermission(RolePermission.newBuilder()
                                .addAllPermissions(grpcPermissions)
                                .addAllRoleIds(roleIds)
                                .build()))
                .build();
    }

    @Override
    public ListResponse listPermissions(ListPermissionsRequest request) {
        return null;
    }

    @Override
    public ListResponse listPermissionCategories(ListPermissionCategoryRequest request) {
        return null;
    }
}
