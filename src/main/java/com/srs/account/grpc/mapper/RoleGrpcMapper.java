package com.srs.account.grpc.mapper;

import com.srs.account.Role;
import com.srs.account.entity.RoleEntity;
import com.srs.common.Status;
import com.srs.proto.mapper.BaseGrpcMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class RoleGrpcMapper implements BaseGrpcMapper<RoleEntity, Role> {

    public Role.Builder toGrpcBuilder(RoleEntity entity) {
        return Role.newBuilder()
                .setRoleId(entity.getRoleId().toString())
                .setCode(entity.getCode())
                .setDescription(Objects.requireNonNullElse(entity.getDescription(), ""))
                .setName(entity.getName())
                .setStatus(Status.forNumber(entity.getStatus()));
    }

    @Override
    public Role toGrpcMessage(RoleEntity entity) {
        return this.toGrpcBuilder(entity).build();
    }
}