package com.srs.account.grpc.service.impl;

import com.google.protobuf.Any;
import com.srs.common.FindByIdRequest;
import com.srs.common.ListResponse;
import com.srs.account.*;
import com.srs.account.entity.PermissionEntity;
import com.srs.account.grpc.mapper.PermissionCategoryGrpcMapper;
import com.srs.account.grpc.mapper.PermissionGrpcMapper;
import com.srs.account.grpc.service.PermissionGrpcService;
import com.srs.account.repository.PermissionCategoryRepository;
import com.srs.account.repository.PermissionDslRepository;
import com.srs.account.repository.PermissionRepository;
import com.srs.account.repository.RoleDslRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Log4j2
public class PermissionGrpcServiceImpl implements PermissionGrpcService {
    private final PermissionRepository permissionRepository;
    private final PermissionCategoryRepository permissionCategoryRepository;
    private final PermissionDslRepository permissionDslRepository;
    private final RoleDslRepository roleDslRepository;

    private final PermissionGrpcMapper permissionGrpcMapper;
    private final PermissionCategoryGrpcMapper permissionCategoryGrpcMapper;

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
        List<PermissionEntity> entities;
        if (request.getRoleCodes().isBlank()) {
            entities = permissionRepository.findAll();
        } else {
            var roleCodes = Arrays.stream(request.getRoleCodes().split(","))
                    .collect(Collectors.toSet());

            entities = permissionDslRepository.findAllByRoleCodes(roleCodes);
        }

        var permissions = entities.stream()
                .map(permissionGrpcMapper::toGrpcMessage)
                .map(Any::pack)
                .collect(Collectors.toList());

        return ListResponse.newBuilder()
                .setSuccess(true)
                .setData(ListResponse.Data.newBuilder()
                        .addAllItems(permissions)
                        .setTotalElements(permissions.size())
                        .build())
                .build();
    }

    @Override
    public ListResponse listPermissionCategories(ListPermissionCategoryRequest request) {

        var permissionCategories = permissionCategoryRepository.findAllPermissionCategories();

        return ListResponse.newBuilder()
                .setSuccess(true)
                .setData(ListResponse.Data.newBuilder()
                        .addAllItems(permissionCategories.stream()
                                .map(permissionCategoryGrpcMapper::toGrpcMessage)
                                .map(Any::pack)
                                .collect(Collectors.toList()))
                        .setTotalElements(permissionCategories.size())
                        .build())
                .build();
    }
}
