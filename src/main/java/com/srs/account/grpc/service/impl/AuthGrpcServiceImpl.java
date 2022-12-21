package com.srs.account.grpc.service.impl;

import com.srs.common.Error;
import com.srs.common.ErrorCode;
import com.srs.account.AuthResponse;
import com.srs.account.LoginRequest;
import com.srs.account.LoginResponse;
import com.srs.account.entity.PermissionEntity;
import com.srs.account.entity.RoleEntity;
import com.srs.account.entity.UserEntity;
import com.srs.account.grpc.service.AuthGrpcService;
import com.srs.account.repository.PermissionRepository;
import com.srs.account.repository.RoleRepository;
import com.srs.account.repository.UserRepository;
import com.srs.proto.constant.GrpcConstant;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

import static com.srs.proto.constant.GrpcConstant.*;
import static java.util.Objects.requireNonNullElse;

@Service
@Log4j2
@RequiredArgsConstructor
public class AuthGrpcServiceImpl implements AuthGrpcService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public LoginResponse login(LoginRequest request) {
        var email = request.getUsername().trim().toLowerCase();

        var user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new RuntimeException("Invalid username or password");
            }
        }

        return this.issueToken(user);
    }

    private LoginResponse issueToken(UserEntity user) {
        if (user == null) {
            return LoginResponse.newBuilder()
                    .setSuccess(false)
                    .setError(Error.newBuilder()
                            .setCode(ErrorCode.USERNAME_OR_PASSWORD_INVALID)
                            .setMessage("Invalid username or password")
                            .build())
                    .build();
        }

        var roles = roleRepository.findAllByUserId(user.getUserId()).stream()
                .map(RoleEntity::getCode)
                .collect(Collectors.toSet());

        if (roles.contains("PUBLIC_USERS") && roles.size() > 1) {
            roles.remove("PUBLIC_USERS");
        }

        var now = new Date();
        var expiration = Date.from(now.toInstant().plus(365, ChronoUnit.DAYS));

        var additionalClaims = new DefaultClaims();
        additionalClaims.putIfAbsent(EMAIL, user.getEmail());
        additionalClaims.putIfAbsent(ROLES, roles);
        additionalClaims.putIfAbsent(USER_ID, user.getUserId().toString());
        additionalClaims.putIfAbsent(FIRST_NAME, user.getFirstName());
        additionalClaims.putIfAbsent(LAST_NAME, user.getLastName());

//        var divisions = Arrays.stream(user.getDivisions().split(","))
//                .filter(StringUtils::isNotBlank)
//                .map(Integer::parseInt)
//                .collect(Collectors.toList());
//        additionalClaims.putIfAbsent(DIVISIONS, divisions);
//
        var marketCodes = Arrays.stream(requireNonNullElse(user.getMarketCodes(), "").split(","))
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
        additionalClaims.putIfAbsent(MARKET_CODES, marketCodes);

        var permissions = permissionRepository.findAllByUserId(user.getUserId()).stream()
                .map(PermissionEntity::getCode)
                .collect(Collectors.toSet());

        additionalClaims.putIfAbsent(PERMISSIONS, permissions);

        var token = Jwts.builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                .setId(user.getUserId().toString())
                .setIssuedAt(now)
                .setNotBefore(now)
                .setExpiration(expiration)
                .addClaims(additionalClaims)
                .signWith(SignatureAlgorithm.HS256, GrpcConstant.JWT_SIGNING_KEY)
                .compact();

        return LoginResponse.newBuilder()
                .setSuccess(true)
                .setData(LoginResponse.Data.newBuilder()
                        .setAccessToken(token)
                        .setUser(AuthResponse.newBuilder()
                                .setUserId(user.getUserId().toString())
                                .setFirstName(user.getFirstName())
                                .setLastName(user.getLastName())
                                .setEmail(user.getEmail())
                                .setUsername(user.getEmail())
                                .addAllRoles(roles)
                                .build())
                        .build())
                .build();
    }
}
