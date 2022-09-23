package com.srs.account.repository;

import com.srs.account.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {
    @Query("select r from RoleEntity r " +
            "inner join UserRoleEntity ur on ur.role.roleId = r.roleId " +
            "where ur.user.userId = :userId")
    List<RoleEntity> findAllByUserId(@Param("userId") UUID userId);
}
