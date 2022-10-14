package com.srs.account.util.validator;

import com.srs.common.Error;
import com.srs.common.ErrorCode;
import com.srs.common.NoContentResponse;


public abstract class BaseValidator {
    protected NoContentResponse asValidationResponse(Error.Builder error) {
        var result = NoContentResponse.newBuilder();

        if (error.getDetailsCount() > 0) {
            error.setCode(ErrorCode.BAD_REQUEST).setMessage("Failed to validate request");
            result.setSuccess(false).setError(error.build());
        } else {
            result.setSuccess(true);
        }

        return result.build();
    }
}
