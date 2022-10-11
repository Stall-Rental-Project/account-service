package com.srs.account.grpc.util;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class RoleUtil {

    public String generateRoleCode(String roleName) {
        if (StringUtils.isBlank(roleName)) {
            throw new IllegalArgumentException("Invalid role name was given");
        }

        return Arrays.stream(roleName.split(" "))
                .map(String::toUpperCase)
                .collect(Collectors.joining("_"));
    }

}
