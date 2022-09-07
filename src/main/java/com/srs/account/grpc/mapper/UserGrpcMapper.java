package com.srs.account.grpc.mapper;

import com.market.account.User;
import com.srs.account.entity.UserEntity;
import com.srs.proto.mapper.BaseGrpcMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserGrpcMapper implements BaseGrpcMapper<UserEntity, User> {

    @Override
    public User toGrpcMessage(UserEntity entity) {
        return null;
    }
}
