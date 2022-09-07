package com.srs.account.repository;

import com.srs.account.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, UUID> {
    @Query("select p from PermissionEntity p " +
            "inner join RolePermissionEntity rp on rp.permissionId = p.permissionId " +
            "inner join UserRoleEntity ur on rp.roleId = ur.role.roleId " +
            "where ur.user.userId = :userId")
    List<PermissionEntity> findAllByUserId(@Param("userId") UUID userId);
}
