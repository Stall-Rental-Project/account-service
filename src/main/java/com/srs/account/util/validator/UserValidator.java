package com.srs.account.util.validator;

import com.market.account.UpsertUserRequest;
import com.market.common.NoContentResponse;
import com.srs.proto.dto.GrpcPrincipal;

/**
 * @author duynt on 2/19/22
 */
public interface UserValidator {
    NoContentResponse validateUpdateUser(UpsertUserRequest request, GrpcPrincipal principal);
}
