package com.srs.account.grpc.mapper;

import com.srs.account.PermissionCategory;
import com.srs.account.entity.PermissionCategoryEntity;
import com.srs.proto.mapper.BaseGrpcMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PermissionCategoryGrpcMapper implements BaseGrpcMapper<PermissionCategoryEntity, PermissionCategory> {

    private final PermissionGrpcMapper permissionGrpcMapper;

    @Override
    public PermissionCategory toGrpcMessage(PermissionCategoryEntity entity) {
        return PermissionCategory.newBuilder()
                .setPermissionCategoryId(entity.getPermissionCategoryId().toString())
                .setDescription(Objects.requireNonNullElse(entity.getDescription(), ""))
                .setName(entity.getName())
                .addAllPermissions(entity.getPermissions().stream()
                        .map(permissionGrpcMapper::toGrpcMessage)
                        .collect(Collectors.toList()))
                .build();
    }
}