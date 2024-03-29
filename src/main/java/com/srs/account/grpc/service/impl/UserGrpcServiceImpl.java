package com.srs.account.grpc.service.impl;

import com.google.protobuf.Any;
import com.srs.account.*;
import com.srs.account.common.Constant;
import com.srs.account.entity.PermissionEntity;
import com.srs.account.entity.RoleEntity;
import com.srs.account.entity.UserEntity;
import com.srs.account.entity.UserRoleEntity;
import com.srs.account.grpc.mapper.UserGrpcMapper;
import com.srs.account.grpc.service.UserGrpcService;
import com.srs.account.grpc.util.PageUtil;
import com.srs.account.repository.*;
import com.srs.account.util.UserUtil;
import com.srs.account.util.validator.UserValidator;
import com.srs.common.FindByCodeRequest;
import com.srs.common.ListResponse;
import com.srs.common.NoContentResponse;
import com.srs.common.PageResponse;
import com.srs.common.dto.Pair;
import com.srs.common.exception.ObjectNotFoundException;
import com.srs.proto.dto.GrpcPrincipal;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@RequiredArgsConstructor
public class UserGrpcServiceImpl implements UserGrpcService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    private final UserDslRepository userDslRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserRoleDslRepository userRoleDslRepository;
    private final UserGrpcMapper userGrpcMapper;

    private final UserValidator userValidator;

    private final Logger log = LoggerFactory.getLogger(UserGrpcServiceImpl.class);
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public NoContentResponse createUser(UpsertUserRequest request, GrpcPrincipal principal) {
        var validationResult = userValidator.validateUpdateUser(request, principal);

        if (!validationResult.getSuccess()) {
            return validationResult;
        }
        var publicUserRoleId = userRoleDslRepository.findPublicUserRoleId()
                .orElseThrow(() -> new ObjectNotFoundException("Public User role not found"));

        var user = new UserEntity();
        var addedUserRoles = new ArrayList<UserRoleEntity>();

        for (var roleIdString : request.getRoleIdsList()) {
            if (StringUtils.isBlank(roleIdString)) {
                continue;
            }
            var roleId = UUID.fromString(roleIdString);
            if (roleId.equals(publicUserRoleId)) {
                continue; // We cannot assign public_user role
            }
            addedUserRoles.add(UserRoleEntity.from(user, new RoleEntity(roleId)));
        }
        this.createUser(user, request);

        userRepository.save(user);

        if (addedUserRoles.size() > 0) {
            userRoleRepository.saveAll(addedUserRoles);
        } else {
            //if don't have any role. Default User Role
            addedUserRoles.add(UserRoleEntity.from(user, new RoleEntity(publicUserRoleId)));
            userRoleRepository.saveAll(addedUserRoles);
        }

        return NoContentResponse.newBuilder()
                .setSuccess(true)
                .build();
    }

    @Override
    @Transactional
    public NoContentResponse updateUser(UpsertUserRequest request, GrpcPrincipal principal) throws Exception {
        var validationResult = userValidator.validateUpdateUser(request, principal);

        if (!validationResult.getSuccess()) {
            return validationResult;
        }

        var user = userRepository.findById(UUID.fromString(request.getUserId()))
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));

        var assignedRoleIds = userRoleDslRepository.findAllRoleIdsByUserId(user.getUserId());

        var publicUserRoleId = userRoleDslRepository.findPublicUserRoleId()
                .orElseThrow(() -> new ObjectNotFoundException("Public User role not found"));

        var removedRoleIds = new HashSet<UUID>();
        var addedUserRoles = new ArrayList<UserRoleEntity>();

        for (var roleIdString : request.getRoleIdsList()) {
            if (StringUtils.isBlank(roleIdString)) {
                continue;
            }
            var roleId = UUID.fromString(roleIdString);
            if (roleId.equals(publicUserRoleId)) {
                continue; // We cannot assign public_user role
            }
            if (!assignedRoleIds.contains(roleId)) {
                addedUserRoles.add(UserRoleEntity.from(user, new RoleEntity(roleId)));
            }
        }


        for (var roleId : assignedRoleIds) {
            if (!request.getRoleIdsList().contains(roleId.toString())) {
                removedRoleIds.add(roleId);
            }
            if(roleId.equals(publicUserRoleId)&&addedUserRoles.size()>0){
                removedRoleIds.add(roleId);
            }
        }

        var hasChangedOnUser = updateUser(user, request);

        if (hasChangedOnUser) {
            userRepository.save(user);
        }
        if (addedUserRoles.size() > 0) {
            userRoleRepository.saveAll(addedUserRoles);
        }

        if (removedRoleIds.size() > 0) {
            userRoleRepository.deleteAllByUserIdAndRoleIds(user.getUserId(), removedRoleIds);
        }
        assignedRoleIds.removeAll(removedRoleIds);
        if(assignedRoleIds.size()>0){
            userRoleRepository.save(UserRoleEntity.from(user, new RoleEntity(publicUserRoleId)));
        }


        return NoContentResponse.newBuilder()
                .setSuccess(true)
                .build();
    }

    @Override
    public GetUserResponse getUser(FindByCodeRequest request, GrpcPrincipal principal) {
        var userId = UUID.fromString(request.getCode());

        var user = this.getUserById(userId, principal);

        return GetUserResponse.newBuilder()
                .setSuccess(true)
                .setData(GetUserResponse.Data.newBuilder()
                        .setUser(user)
                        .build())
                .build();
    }

    @Override
    public GetCurrentUserResponse getCurrentUser(GrpcPrincipal principal) {
        var userId = principal.getUserId();

        var user = this.getUserById(userId, principal);

        return GetCurrentUserResponse.newBuilder()
                .setSuccess(true)
                .setData(GetCurrentUserResponse.Data.newBuilder()
                        .setUser(user)
                        .build())
                .build();
    }

    private User getUserById(UUID userId, GrpcPrincipal principal) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));

        var roles = roleRepository.findAllByUserId(user.getUserId());

        var grpcRoleNames = new ArrayList<String>();
        var grpcRoleIds = new ArrayList<String>();
        var grpcRoleCodes = new ArrayList<String>();
        for (RoleEntity role : roles) {
            grpcRoleNames.add(role.getName());
            grpcRoleIds.add(role.getRoleId().toString());
            grpcRoleCodes.add(role.getCode());
        }
        grpcRoleNames.sort(String::compareTo);

        var grpcUser = userGrpcMapper.toGrpcBuilder(user)
                .addAllRoles(grpcRoleNames)
                .addAllRoleIds(grpcRoleIds)
                .addAllRoleCodes(grpcRoleCodes);

        if (userId.equals(principal.getUserId())) {
            var permissions = permissionRepository.findAllByUserId(user.getUserId()).stream()
                    .map(PermissionEntity::getCode)
                    .collect(Collectors.toSet());
            grpcUser.addAllPermissions(permissions);
        }

        return grpcUser.build();
    }


    @Override
    public ListResponse listUsersByEmail(FindByCodeRequest request, GrpcPrincipal principal) {
        var email = request.getCode();

        var users = userRepository.findAllPublicUsersByEmail(email.trim()).stream()
                .map(userGrpcMapper::toGrpcMessage)
                .map(Any::pack)
                .collect(Collectors.toList());

        return ListResponse.newBuilder()
                .setSuccess(true)
                .setData(ListResponse.Data.newBuilder()
                        .addAllItems(users)
                        .setTotalElements(users.size())
                        .build())
                .build();
    }

    @Override
    public PageResponse listUsers(ListUsersRequest request, GrpcPrincipal principal) {
        var pageResult = userDslRepository.findAll(request, principal);
        var pageRequest = PageUtil.normalizeRequest(request.getPageRequest(), Constant.USER_SORTS);

        var userIds = pageResult.getItems().stream()
                .map(UserEntity::getUserId)
                .collect(Collectors.toSet());

        var userRoles = userRoleDslRepository.findAllRolesByUserIdsConcatAsString(userIds);

        var userRoleMap = new HashMap<UUID, Pair<List<String>, List<String>>>();
        for (var tuple : userRoles) {
            var userId = tuple.get(0, UUID.class);
            var roleNames = tuple.get(1, String.class);
            var roleIds = tuple.get(2, String.class);
            assert roleNames != null && roleIds != null;
            var roleNameList = Arrays.stream(roleNames.split(","))
                    .sorted(String::compareTo)
                    .collect(Collectors.toUnmodifiableList());
            var roleIdList = Arrays.stream(roleIds.split(","))
                    .collect(Collectors.toUnmodifiableList());
            userRoleMap.put(userId, com.srs.common.dto.Pair.from(roleIdList, roleNameList));
        }

        var grpcUsers = new ArrayList<Any>();
        for (var user : pageResult.getItems()) {
            var grpcUser = userGrpcMapper.toGrpcBuilder(user);
            var grpcRoles = userRoleMap.getOrDefault(user.getUserId(), Pair.from(Collections.emptyList(), Collections.emptyList()));
            var grpcRoleIds = grpcRoles.getFirst();
            var grpcRoleNames = grpcRoles.getSecond();
            grpcUser.addAllRoles(grpcRoleNames);
            grpcUser.addAllRoleIds(grpcRoleIds);
            grpcUsers.add(Any.pack(grpcUser.build()));
        }

        return PageResponse.newBuilder()
                .setSuccess(true)
                .setData(PageResponse.Data.newBuilder()
                        .setPage(pageRequest.getPage())
                        .setSize(pageRequest.getSize())
                        .setTotalElements(pageResult.getTotal())
                        .setTotalPages(PageUtil.calcTotalPages(pageResult.getTotal(), pageRequest.getSize()))
                        .addAllItems(grpcUsers)
                        .build())
                .build();
    }

    @Override
    @Transactional
    public NoContentResponse deleteUser(FindByCodeRequest request, GrpcPrincipal principal) {
        var userId = UUID.fromString(request.getCode());

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));
        userRoleRepository.deleteByUserId(user.getUserId());
        userRepository.delete(user);

        return NoContentResponse.newBuilder()
                .setSuccess(true)
                .build();
    }

    private void createUser(UserEntity entity, UpsertUserRequest request) {
        entity.setFirstName(request.getFirstName());
        entity.setMiddleName(request.getMiddleName());
        entity.setLastName(request.getLastName());
        entity.setStatus(request.getStatusValue());
        entity.setEmail(request.getEmail());
        entity.setPassword(passwordEncoder.encode(Constant.passwordDefault));
        var marketCodes = UserUtil.grpcToNativeMarketCodes(request.getMarketCodesList());
        entity.setMarketCodes(marketCodes);
    }

    private boolean updateUser(UserEntity entity, UpsertUserRequest request) {
        boolean hasChanged = false;

        if (isNotBlank(request.getFirstName()) && !Objects.equals(entity.getFirstName(), request.getFirstName())) {
            entity.setFirstName(request.getFirstName());
            hasChanged = true;
        }

        if (isNotBlank(request.getMiddleName()) && !Objects.equals(entity.getMiddleName(), request.getMiddleName())) {
            entity.setMiddleName(request.getMiddleName());
            hasChanged = true;
        }

        if (isNotBlank(request.getLastName()) && !Objects.equals(entity.getLastName(), request.getLastName())) {
            entity.setLastName(request.getLastName());
            hasChanged = true;
        }
        if (isNotBlank(request.getEmail()) && !Objects.equals(entity.getEmail(), request.getEmail())) {
            entity.setEmail(request.getEmail());
            hasChanged = true;
        }
        if (entity.getStatus() != request.getStatusValue()) {
            entity.setStatus(request.getStatusValue());
            hasChanged = true;
        }

        var marketCodes = UserUtil.grpcToNativeMarketCodes(request.getMarketCodesList());

        if (!Objects.equals(marketCodes, entity.getMarketCodes())) {
            entity.setMarketCodes(marketCodes);
            hasChanged = true;
        }

        return hasChanged;
    }


}
