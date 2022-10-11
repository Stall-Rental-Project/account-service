package com.srs.account.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.srs.account.ListRoleRequest;
import com.srs.account.common.Constant;
import com.srs.account.entity.QRoleEntity;
import com.srs.account.entity.QUserRoleEntity;
import com.srs.account.entity.RoleEntity;
import com.srs.account.grpc.util.PageUtil;
import com.srs.common.domain.Page;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
@RequiredArgsConstructor
public class RoleDslRepository {
    private final QRoleEntity role = QRoleEntity.roleEntity;
    private final QUserRoleEntity userRole = QUserRoleEntity.userRoleEntity;

    private final JPAQueryFactory queryFactory;

    public List<UUID> findAllIdsByUserId(UUID userId) {
        JPAQuery<UUID> query = queryFactory.selectDistinct(role.roleId)
                .from(role)
                .innerJoin(userRole).on(role.roleId.eq(userRole.role.roleId))
                .where(userRole.user.userId.eq(userId));

        return query.fetch();
    }

    public boolean existsByName(String name) {
        JPAQuery<Long> query = queryFactory.select(role.count())
                .from(role)
                .where(role.name.equalsIgnoreCase(name.trim()));

        return query.fetchFirst() > 0;
    }

    public Page<RoleEntity> findAll(ListRoleRequest request) {
        JPAQuery<?> baseQuery = queryFactory.from(role);

        if (!request.getIncludePublic()) {
            baseQuery.where(role.code.ne("PUBLIC_USERS"));
        }

        if (StringUtils.isNotBlank(request.getName())) {
            baseQuery.where(role.name.containsIgnoreCase(request.getName())
                    .or(role.description.containsIgnoreCase(request.getName()))
            );
        }

        if (request.hasStatus()) {
            baseQuery.where(role.status.eq(request.getStatusValue()));
        }

        var pageRequest = PageUtil.normalizeRequest(request.getPageRequest(), Constant.ROLE_SORTS);
        var sortDirection = "DESC".equalsIgnoreCase(pageRequest.getDirection()) ? Order.DESC : Order.ASC;

        JPAQuery<Long> countQuery = baseQuery.clone().select(role.count());
        JPAQuery<RoleEntity> selectQuery = baseQuery.clone().select(role)
                .limit(pageRequest.getSize())
                .offset(PageUtil.calcPageOffset(pageRequest.getPage(), pageRequest.getSize()));

        if (pageRequest.getSort().equals("status")) {
            selectQuery.orderBy(new OrderSpecifier<>(sortDirection, role.status));
        } else {
            selectQuery.orderBy(pageRequest.getDirection().equalsIgnoreCase("desc") ? role.name.lower().desc() : role.name.lower().asc());

        }

        return Page.from(selectQuery.fetch(), countQuery.fetchFirst());
    }
}
