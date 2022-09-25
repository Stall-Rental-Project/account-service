package com.srs.account.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.srs.account.entity.QRoleEntity;
import com.srs.account.entity.QUserRoleEntity;
import lombok.RequiredArgsConstructor;
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
}
