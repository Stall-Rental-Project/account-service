package com.srs.account.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.srs.account.entity.QPermissionEntity;
import com.srs.account.entity.QRolePermissionEntity;
import com.srs.account.entity.QUserEntity;
import com.srs.account.entity.QUserRoleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserDslRepository {
    private final QUserEntity user = QUserEntity.userEntity;
    private final QUserRoleEntity userRole = QUserRoleEntity.userRoleEntity;
    private final QRolePermissionEntity rolePermission = QRolePermissionEntity.rolePermissionEntity;
    private final QPermissionEntity permission = QPermissionEntity.permissionEntity;

    private final JPAQueryFactory queryFactory;
}
