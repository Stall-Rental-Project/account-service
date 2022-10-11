package com.srs.account.util.validator.impl;

import com.market.account.UpsertUserRequest;
import com.market.common.Error;
import com.market.common.NoContentResponse;
import com.market.common.Status;
import com.srs.account.repository.RoleRepository;
import com.srs.account.util.validator.BaseValidator;
import com.srs.account.util.validator.UserValidator;
import com.srs.proto.dto.GrpcPrincipal;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class UserValidatorImpl extends BaseValidator implements UserValidator {
    private final RoleRepository roleRepository;

    @Override
    public NoContentResponse validateUpdateUser(UpsertUserRequest request, GrpcPrincipal principal) {
        var error = Error.newBuilder();


        if (request.getStatus().equals(Status.UNRECOGNIZED)) {
            error.putDetails("status", "Invalid status");
        }

        if (request.getRoleIdsCount() > 0) {
            var roleIds = request.getRoleIdsList().stream()
                    .filter(StringUtils::isNotBlank)
                    .map(UUID::fromString)
                    .collect(Collectors.toSet());

            var roles = roleRepository.findAllById(roleIds);
            if (roleIds.size() > roles.size()) {
                error.putDetails("role_ids", "Some given role(s) not found");
            }
        }
        return asValidationResponse(error);
    }
}
