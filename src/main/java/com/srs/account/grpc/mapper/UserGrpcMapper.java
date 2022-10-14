package com.srs.account.grpc.mapper;

import com.srs.account.User;
import com.srs.account.entity.UserEntity;
import com.srs.account.util.UserUtil;
import com.srs.proto.mapper.BaseGrpcMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.util.Objects.requireNonNullElse;

@Component
@RequiredArgsConstructor
public class UserGrpcMapper implements BaseGrpcMapper<UserEntity, User> {

    public User.Builder toGrpcBuilder(UserEntity entity) {
        return User.newBuilder()
                .setUserId(entity.getUserId().toString())
                .setEmail(entity.getEmail())
                .setFirstName(requireNonNullElse(entity.getFirstName(), ""))
                .setMiddleName(requireNonNullElse(entity.getMiddleName(), ""))
                .setLastName(requireNonNullElse(entity.getLastName(), ""))
                .setStatusValue(entity.getStatus())
                .addAllMarketCodes(UserUtil.nativeToGrpcMarketCodes(entity.getMarketCodes()));
    }

    @Override
    public User toGrpcMessage(UserEntity entity) {
        return this.toGrpcBuilder(entity).build();
    }

}
