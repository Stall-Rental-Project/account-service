package com.srs.account.grpc.service.impl;

import com.market.account.UpsertUserRequest;
import com.market.common.NoContentResponse;
import com.srs.account.grpc.mapper.UserGrpcMapper;
import com.srs.account.grpc.service.UserGrpcService;
import com.srs.account.kafka.producer.DemoKafka;
import com.srs.account.repository.PermissionRepository;
import com.srs.account.repository.RoleRepository;
import com.srs.account.repository.UserRepository;
import com.srs.account.repository.UserRoleRepository;
import com.srs.common.kafka.message.market.DemoKafkaMessage;
import com.srs.proto.dto.GrpcPrincipal;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserGrpcServiceImpl implements UserGrpcService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserGrpcMapper userGrpcMapper;
    private final Logger log = LoggerFactory.getLogger(UserGrpcServiceImpl.class);
    private final DemoKafka demoKafkaProducer;
    @Override
    @Transactional
    public NoContentResponse createUser(UpsertUserRequest request, GrpcPrincipal principal) throws Exception {
        var marketMessage = new DemoKafkaMessage();
        marketMessage.setMarketName("Hoang ngo");
        marketMessage.setMarketType(2);
        marketMessage.setBarangayId(UUID.fromString("0221e7fb-54ef-4f20-b4d3-af95e53ed662"));
        marketMessage.setMappedLocation("hue city");
        marketMessage.setStreet("Ngo Si Lien");
        demoKafkaProducer.sendMessageWhenCallUserApi(marketMessage, principal);
        userRepository.save(userGrpcMapper.createUser(request));
        return NoContentResponse.newBuilder()
                .setSuccess(true)
                .build();
    }

}
