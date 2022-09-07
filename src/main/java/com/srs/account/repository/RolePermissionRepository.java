package com.srs.account.repository;

import com.srs.account.entity.RolePermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.UUID;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermissionEntity, UUID> {

    @Transactional
    @Modifying
    @Query("delete from RolePermissionEntity rp where rp.roleId = :roleId")
    void deleteByRoleId(@Param("roleId") UUID roleId);

    @Transactional
    @Modifying
    @Query("delete from RolePermissionEntity rp where rp.roleId in (:roleIds)")
    void deleteByRoleIds(@Param("roleIds") Collection<UUID> roleIds);
}
