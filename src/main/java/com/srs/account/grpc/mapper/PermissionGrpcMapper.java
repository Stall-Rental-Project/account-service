package com.srs.account.grpc.mapper;

import com.srs.account.Permission;
import com.srs.account.entity.PermissionEntity;
import com.srs.proto.mapper.BaseGrpcMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class PermissionGrpcMapper implements BaseGrpcMapper<PermissionEntity, Permission> {

    @Override
    public Permission toGrpcMessage(PermissionEntity entity) {
        return this.toGrpcBuilder(entity, false).build();
    }

    public Permission.Builder toGrpcBuilder(PermissionEntity entity, boolean fetchCategory) {
        var permission = Permission.newBuilder()
                .setPermissionId(entity.getPermissionId().toString())
                .setCode(entity.getCode())
                .setName(entity.getName());

        if (fetchCategory) {
            permission.setPermissionCategory(entity.getPermissionCategory().getName());
        }

        return permission;
    }
}
