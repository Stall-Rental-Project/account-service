package com.srs.account.grpc.mapper;

import com.market.account.UpsertUserRequest;
import com.market.account.User;
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
                .setExternalId(entity.getExternalId())
                .setFirstName(requireNonNullElse(entity.getFirstName(), ""))
                .setMiddleName(requireNonNullElse(entity.getMiddleName(), ""))
                .setLastName(requireNonNullElse(entity.getLastName(), ""))
                .setStatusValue(entity.getStatus())
                .addAllMarketCodes(UserUtil.nativeToGrpcMarketCodes(entity.getMarketCodes()))
                .addAllDivisions(UserUtil.nativeToGrpcMarketDivisions(entity.getDivisions()));
    }

    @Override
    public User toGrpcMessage(UserEntity entity) {
        return this.toGrpcBuilder(entity).build();
    }

    public UserEntity createUser(UpsertUserRequest request){
        var user=new UserEntity();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword("P1@zz@2022");
        user.setMiddleName(request.getMiddleName());
        user.setExternalId("0221e7fb-54ef-4f20-b4d3-af95e53ed662");
        return user;
    }

}
