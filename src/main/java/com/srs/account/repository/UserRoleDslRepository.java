package com.srs.account.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.srs.account.entity.QRoleEntity;
import com.srs.account.entity.QUserEntity;
import com.srs.account.entity.QUserRoleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRoleDslRepository {
    private final QRoleEntity role = QRoleEntity.roleEntity;
    private final QUserRoleEntity userRole = QUserRoleEntity.userRoleEntity;
    private final QUserEntity user = QUserEntity.userEntity;

    private final JPAQueryFactory queryFactory;

    public Optional<UUID> findPublicUserRoleId() {
        JPAQuery<UUID> query = queryFactory.select(role.roleId)
                .from(role)
                .where(role.code.eq("PUBLIC_USERS"));

        return Optional.ofNullable(query.fetchFirst());
    }

    public List<UUID> findAllRoleIdsByUserId(UUID userId) {
        JPAQuery<UUID> query = queryFactory.select(userRole.role.roleId)
                .from(userRole)
                .where(userRole.user.userId.eq(userId));

        return query.fetch();
    }

    public List<Tuple> findAllRolesByUserIdsConcatAsString(Collection<UUID> userIds) {
        JPAQuery<Tuple> query = queryFactory.select(
                        user.userId,
                        Expressions.stringTemplate("STRING_AGG({0}, ',')", role.name).coalesce("").as("role_names"),
                        Expressions.stringTemplate("STRING_AGG({0}, ',')", Expressions.stringOperation(Ops.STRING_CAST, role.roleId)).coalesce("").as("role_ids"),
                        Expressions.stringTemplate("STRING_AGG({0}, ',')", role.code).coalesce("").as("role_codes"))
                .from(user)
                .innerJoin(userRole).on(user.userId.eq(userRole.user.userId))
                .innerJoin(role).on(userRole.role.roleId.eq(role.roleId))
                .where(user.userId.in(userIds))
                .groupBy(user.userId);

        return query.fetch();
    }
}
