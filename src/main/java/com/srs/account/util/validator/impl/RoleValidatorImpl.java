package com.srs.account.util.validator.impl;

import com.srs.common.Error;
import com.srs.common.ErrorCode;
import com.srs.common.NoContentResponse;
import com.srs.common.Status;
import com.srs.account.UpsertRoleRequest;
import com.srs.account.repository.RoleDslRepository;
import com.srs.account.repository.RoleRepository;
import com.srs.account.util.validator.BaseValidator;
import com.srs.account.util.validator.RoleValidator;
import com.srs.proto.dto.GrpcPrincipal;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RoleValidatorImpl extends BaseValidator implements RoleValidator {

    private final RoleDslRepository roleDslRepository;

    private final RoleRepository roleRepository;

    @Override
    public NoContentResponse validateCreateRoleRequest(UpsertRoleRequest request) {
        var result = this.validate(request);

        if (!result.getSuccess()) {
            return result;
        }

        if (roleDslRepository.existsByName(request.getName())) {
            return NoContentResponse.newBuilder()
                    .setSuccess(false)
                    .setError(Error.newBuilder()
                            .setCode(ErrorCode.INVALID_PARAMETERS)
                            .setMessage("Role's name already exist")
                            .build())
                    .build();
        }

        return NoContentResponse.newBuilder()
                .setSuccess(true)
                .build();
    }

    @Override
    public NoContentResponse validateUpdateRoleRequest(UpsertRoleRequest request, GrpcPrincipal identity) {
        var result = this.validate(request);

        if (!result.getSuccess()) {
            return result;
        }

        var roleId = UUID.fromString(request.getRoleId());
        var existingRole = roleRepository.findByNameIgnoreCase(request.getName()).orElse(null);

        if (existingRole != null && !existingRole.getRoleId().equals(roleId)) {
            return NoContentResponse.newBuilder()
                    .setSuccess(false)
                    .setError(Error.newBuilder()
                            .setCode(ErrorCode.INVALID_PARAMETERS)
                            .setMessage("Role's name already exists")
                            .build())
                    .build();
        }

        existingRole = roleRepository.findById(roleId).orElse(null);

        if (existingRole == null) {
            return NoContentResponse.newBuilder()
                    .setSuccess(false)
                    .setError(Error.newBuilder()
                            .setCode(ErrorCode.INVALID_PARAMETERS)
                            .setMessage("Role not found")
                            .build())
                    .build();
        }

        if (Status.INACTIVE.equals(request.getStatus())
                && existingRole.getStatus() == Status.ACTIVE_VALUE
                && roleRepository.existsRoleAlreadyUsedByUser(roleId)) {
            return NoContentResponse.newBuilder()
                    .setSuccess(false)
                    .setError(Error.newBuilder()
                            .setCode(ErrorCode.ROLE_ALREADY_EXISTS)
                            .setMessage("Role is already in use, cannot inactivate")
                            .build())
                    .build();
        }

        return NoContentResponse.newBuilder()
                .setSuccess(true)
                .build();
    }

    private NoContentResponse validate(UpsertRoleRequest request) {
        if (StringUtils.isBlank(request.getName())) {
            return NoContentResponse.newBuilder()
                    .setSuccess(false)
                    .setError(Error.newBuilder()
                            .setCode(ErrorCode.MISSING_REQUIRE_FIELDS)
                            .setMessage("Name is required")
                            .build())
                    .build();
        }

        return NoContentResponse.newBuilder()
                .setSuccess(true)
                .build();
    }
}
