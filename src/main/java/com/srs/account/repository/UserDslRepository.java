package com.srs.account.repository;

import com.market.account.ListUsersRequest;
import com.market.common.PageRequest;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.srs.account.common.Constant;
import com.srs.account.entity.*;
import com.srs.account.grpc.util.PageUtil;
import com.srs.common.domain.Page;
import com.srs.proto.dto.GrpcPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@Log4j2
@RequiredArgsConstructor
public class UserDslRepository {
    private final QUserEntity user = QUserEntity.userEntity;
    private final QUserRoleEntity userRole = QUserRoleEntity.userRoleEntity;
    private final QRolePermissionEntity rolePermission = QRolePermissionEntity.rolePermissionEntity;
    private final QPermissionEntity permission = QPermissionEntity.permissionEntity;

    private final JPAQueryFactory queryFactory;

    public Page<UserEntity> findAll(ListUsersRequest request, GrpcPrincipal principal) {
        JPAQuery<?> baseQuery = queryFactory.from(user);

        if (!request.getIncludePublic()) {
            baseQuery.where(queryFactory.selectZero()
                    .from(userRole)
                    .where(userRole.user.userId.eq(user.userId))
                    .where(userRole.role.code.eq("PUBLIC_USERS"))
                    .notExists());
        }

        if (StringUtils.isNotBlank(request.getSearchTerm())) {
            if (request.getSearchEmailOnly()) {
                baseQuery.where(user.email.containsIgnoreCase(request.getSearchTerm()));
            } else {
                baseQuery.where((user.email.containsIgnoreCase(request.getSearchTerm()))
                        .or(user.firstName.containsIgnoreCase(request.getSearchTerm()))
                        .or(user.lastName.containsIgnoreCase(request.getSearchTerm())));
            }
        }

        if (request.hasStatus()) {
            baseQuery.where(user.status.eq(request.getStatus().getNumber()));
        }

        if (request.getRoleIdsCount() > 0) {
            var roleIds = request.getRoleIdsList().stream()
                    .map(UUID::fromString)
                    .collect(Collectors.toSet());

            baseQuery.where(queryFactory.selectZero()
                    .from(userRole)
                    .where(userRole.user.userId.eq(user.userId))
                    .where(userRole.role.roleId.in(roleIds))
                    .exists());
        }

        // TODO: filter by market if user if market-owned user

        var pageRequest = PageUtil.normalizeRequest(request.getPageRequest(), Constant.USER_SORTS);

        JPAQuery<UserEntity> selectQuery = baseQuery.clone()
                .select(user)
                .limit(pageRequest.getSize())
                .offset(PageUtil.calcPageOffset(pageRequest.getPage(), pageRequest.getSize()))
                .orderBy(getUserOrderSpecifier(pageRequest));

        JPAQuery<Long> countQuery = baseQuery.clone()
                .select(user.userId.count());

        return Page.from(selectQuery.fetch(), countQuery.fetchFirst());
    }

    private OrderSpecifier<?> getUserOrderSpecifier(PageRequest pageRequest) {
        var order = Order.valueOf(pageRequest.getDirection());
        switch (pageRequest.getSort()) {
            case "email":
                return new OrderSpecifier<>(order, user.email.lower());
            case "status":
                return new OrderSpecifier<>(order, user.status);
            default:
                return new OrderSpecifier<>(order, user.firstName.lower());
        }
    }
}
