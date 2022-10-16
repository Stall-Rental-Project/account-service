package com.srs.account.grpc.service.impl;

import com.google.protobuf.Any;
import com.srs.common.*;
import com.srs.account.GetRoleResponse;
import com.srs.account.ListRoleRequest;
import com.srs.account.UpsertRoleRequest;
import com.srs.account.common.Constant;
import com.srs.account.entity.PermissionEntity;
import com.srs.account.entity.RoleEntity;
import com.srs.account.entity.RolePermissionEntity;
import com.srs.account.grpc.mapper.RoleGrpcMapper;
import com.srs.account.grpc.service.RoleGrpcService;
import com.srs.account.grpc.util.PageUtil;
import com.srs.account.grpc.util.RoleUtil;
import com.srs.account.repository.*;
import com.srs.account.util.validator.RoleValidator;
import com.srs.common.exception.AccessDeniedException;
import com.srs.common.exception.ObjectNotFoundException;
import com.srs.proto.dto.GrpcPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class RoleGrpcServiceImpl implements RoleGrpcService {

    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final RoleValidator roleValidator;
    private final PermissionRepository permissionRepository;

    private final RoleDslRepository roleDslRepository;
    private final PermissionDslRepository permissionDslRepository;

    private final RoleUtil roleUtil;

    private final RoleGrpcMapper roleGrpcMapper;

    @Override
    public PageResponse listRoles(ListRoleRequest request, GrpcPrincipal principal) {
        var pageRequest = PageUtil.normalizeRequest(request.getPageRequest(), Constant.ROLE_SORTS);
        var pageResponse = roleDslRepository.findAll(request);

        var roles = pageResponse.getItems().stream()
                .map(roleGrpcMapper::toGrpcMessage)
                .map(Any::pack)
                .collect(Collectors.toList());

        return PageResponse.newBuilder()
                .setSuccess(true)
                .setData(PageResponse.Data.newBuilder()
                        .addAllItems(roles)
                        .setSize(pageRequest.getSize())
                        .setTotalPages(PageUtil.calcTotalPages(pageResponse.getTotal(), pageRequest.getSize()))
                        .setTotalElements(pageResponse.getTotal())
                        .setPage(pageRequest.getPage())
                        .build())
                .build();
    }

    @Override
    public GetRoleResponse getRole(FindByIdRequest request, GrpcPrincipal principal) {
        var roleId = UUID.fromString(request.getId());
        var role = roleRepository.findByRoleId(roleId)
                .orElseThrow(() -> new ObjectNotFoundException("Role not found"));

        var permissionIds = permissionDslRepository.findAllIdsByRoleId(roleId);

        var grpcRole = roleGrpcMapper.toGrpcBuilder(role)
                .addAllPermissionIds(permissionIds)
                .build();

        return GetRoleResponse.newBuilder()
                .setSuccess(true)
                .setData(GetRoleResponse.Data.newBuilder()
                        .setRole(grpcRole)
                        .build())
                .build();
    }

    @Override
    @Transactional
    public NoContentResponse createRole(UpsertRoleRequest request, GrpcPrincipal principal) {
        var validateResult = roleValidator.validateCreateRoleRequest(request);

        if (!validateResult.getSuccess()) {
            return validateResult;
        }

        var role = RoleEntity.builder()
                .code(roleUtil.generateRoleCode(request.getName()))
                .name(request.getName())
                .description(request.getDescription())
                .status(request.getStatusValue())
                .build();

        roleRepository.save(role);


        if (request.getPermissionIdsList().size() > 0) {
            var permissionIds = request.getPermissionIdsList().stream()
                    .map(UUID::fromString)
                    .collect(Collectors.toSet());

            var permissionCodes = permissionRepository.findAllById(permissionIds).stream()
                    .map(PermissionEntity::getCode)
                    .collect(Collectors.toList());

            var rolePermissions = permissionIds.stream()
                    .map(permissionId -> RolePermissionEntity.builder()
                            .roleId(role.getRoleId())
                            .permissionId(permissionId)
                            .build())
                    .collect(Collectors.toList());

            rolePermissionRepository.saveAll(rolePermissions);

        }


        return NoContentResponse.newBuilder()
                .setSuccess(true)
                .build();
    }

    @Override
    @Transactional
    public NoContentResponse updateRole(UpsertRoleRequest request, GrpcPrincipal principal) {
        var validateResult = roleValidator.validateUpdateRoleRequest(request, principal);

        if (!validateResult.getSuccess()) {
            return validateResult;
        }

        var roleId = UUID.fromString(request.getRoleId());
        var role = roleRepository.findByRoleId(roleId)
                .orElseThrow(() -> new ObjectNotFoundException("Role not found"));

        if (role.getCode().equals("PUBLIC_USERS")) {
            throw new IllegalArgumentException("Cannot update public user role");
        }

        var previousPermissions = permissionDslRepository.findAllCodesByRoleId(roleId);

        boolean hasChanged = this.updateRole(role, request, principal);


        roleRepository.save(role);

        if (request.getPermissionIdsList().size() > 0) {
            rolePermissionRepository.deleteByRoleId(role.getRoleId());
            var permissionIds = request.getPermissionIdsList().stream()
                    .map(UUID::fromString)
                    .collect(Collectors.toSet());

            var currentPermissions = permissionDslRepository.findAllCodesByPermissionIds(permissionIds);

            previousPermissions.sort(String::compareTo);
            currentPermissions.sort(String::compareTo);

            hasChanged = !String.join(",", previousPermissions).equals(String.join(",", currentPermissions)) || hasChanged;

            var rolePermissions = permissionIds.stream()
                    .map(permissionId -> RolePermissionEntity.builder()
                            .roleId(role.getRoleId())
                            .permissionId(permissionId)
                            .build())
                    .collect(Collectors.toList());

            rolePermissionRepository.saveAll(rolePermissions);
        }


        return NoContentResponse.newBuilder()
                .setSuccess(true)
                .build();
    }

    private boolean updateRole(RoleEntity entity, UpsertRoleRequest request, GrpcPrincipal principal) {
        boolean hasChanged = false;

        if (!Objects.equals(entity.getStatus(), request.getStatusValue())) {
            entity.setStatus(request.getStatusValue());
            hasChanged = true;
        }

        if (!Objects.equals(entity.getName(), request.getName())) {
            entity.setName(request.getName());
            hasChanged = true;
        }

        if (!Objects.equals(entity.getDescription(), request.getDescription())) {
            entity.setDescription(request.getDescription());
            hasChanged = true;
        }

        return hasChanged;
    }

    @Override
    @Transactional
    public NoContentResponse deleteRole(FindByIdRequest request, GrpcPrincipal principal) {
        var builder = BaseResponse.newBuilder();

        var roleId = UUID.fromString(request.getId());
        var role = roleRepository.findById(roleId).orElse(null);

        if (role == null) {
            log.warn("Role {} has been already deleted", roleId);
            return NoContentResponse.newBuilder()
                    .setSuccess(true)
                    .build();
        }

        if (role.getCode().equals("SYSTEM_ADMIN")) {
            throw new AccessDeniedException("Cannot delete the System Admin role");
        }

        if (roleRepository.existsRoleAlreadyUsedByUser(roleId)) {
            builder.setErrorResponse(ErrorResponse.newBuilder()
                    .setErrorCode(ErrorCode.CANNOT_EXECUTE)
                    .setErrorDescription("The role is already in use, cannot delete")
                    .build());
        }

        rolePermissionRepository.deleteByRoleId(roleId);
        roleRepository.delete(role);


        return NoContentResponse.newBuilder()
                .setSuccess(true)
                .build();
    }

    @Override
    public PageResponse findAllByIds(FindByIdsRequest request, GrpcPrincipal principal) {
        var roleIds = request.getIdsList().stream()
                .map(UUID::fromString)
                .collect(Collectors.toSet());

        var roles = roleRepository.findByRoleIds(roleIds);

        return PageResponse.newBuilder()
                .setSuccess(true)
                .setData(PageResponse.Data.newBuilder()
                        .addAllItems(roles.stream()
                                .map(roleGrpcMapper::toGrpcMessage)
                                .map(Any::pack)
                                .collect(Collectors.toList()))
                        .build())
                .build();
    }
}
