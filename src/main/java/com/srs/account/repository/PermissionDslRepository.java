package com.srs.account.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.srs.account.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Repository
@RequiredArgsConstructor
public class PermissionDslRepository {
    private final QPermissionEntity permission = QPermissionEntity.permissionEntity;
    private final QPermissionCategoryEntity permissionCategory = QPermissionCategoryEntity.permissionCategoryEntity;
    private final QRolePermissionEntity rolePermission = QRolePermissionEntity.rolePermissionEntity;
    private final QUserRoleEntity userRole = QUserRoleEntity.userRoleEntity;
    private final QRoleEntity role = QRoleEntity.roleEntity;

    private final JPAQueryFactory queryFactory;

    public List<Tuple> findAllByUserId(UUID userId) {
        JPAQuery<Tuple> query = queryFactory.select(permission.permissionId, permission.code, permission.name, permissionCategory.name)
                .from(permission)
                .innerJoin(permissionCategory)
                .on(permission.permissionCategory.permissionCategoryId.eq(permissionCategory.permissionCategoryId))
                .where(queryFactory.selectZero()
                        .from(rolePermission)
                        .where(rolePermission.permissionId.eq(permission.permissionId))
                        .where(rolePermission.roleId.in(
                                queryFactory.select(userRole.role.roleId)
                                        .from(userRole)
                                        .where(userRole.user.userId.eq(userId)))
                        )
                        .exists());

        return query.fetch();
    }

    public List<String> findAllCodesByRoleId(UUID roleId) {
        JPAQuery<String> query = queryFactory.select(permission.code)
                .from(permission)
                .innerJoin(rolePermission).on(permission.permissionId.eq(rolePermission.permissionId))
                .where(rolePermission.roleId.eq(roleId));

        return query.fetch();
    }

    public List<String> findAllIdsByRoleId(UUID roleId) {
        JPAQuery<UUID> query = queryFactory.select(permission.permissionId)
                .from(permission)
                .innerJoin(rolePermission).on(permission.permissionId.eq(rolePermission.permissionId))
                .where(rolePermission.roleId.eq(roleId));

        return query.fetch().stream()
                .map(UUID::toString)
                .collect(Collectors.toList());
    }

    public List<String> findAllCodesByPermissionIds(Collection<UUID> permissionIds) {
        JPAQuery<String> query = queryFactory.select(permission.code)
                .from(permission)
                .where(permission.permissionId.in(permissionIds));

        return query.fetch();
    }

    public List<Tuple> findAllAndGroupByRole(Collection<String> roleCodes) {
        JPAQuery<Tuple> query = queryFactory.select(role.code, Expressions.stringTemplate("STRING_AGG({0}, ',')", permission.name).coalesce("").as("permissions"))
                .from(role)
                .innerJoin(rolePermission).on(role.roleId.eq(rolePermission.roleId))
                .innerJoin(permission).on(rolePermission.permissionId.eq(permission.permissionId))
                .where(permission.code.in(roleCodes))
                .groupBy(role.code);

        return query.fetch();
    }

    public List<PermissionEntity> findAllByRoleCodes(Collection<String> roleCodes) {
        JPAQuery<PermissionEntity> query = queryFactory.selectDistinct(permission)
                .from(permission)
                .innerJoin(rolePermission).on(permission.permissionId.eq(rolePermission.permissionId))
                .innerJoin(role).on(rolePermission.roleId.eq(role.roleId))
                .where(role.code.in(roleCodes));

        return query.fetch();
    }
}
