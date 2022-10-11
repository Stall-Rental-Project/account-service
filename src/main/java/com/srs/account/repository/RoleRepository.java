package com.srs.account.repository;

import com.srs.account.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {

    Optional<RoleEntity> findByNameIgnoreCase(String name);

    @Query("select r from RoleEntity r " +
            "inner join UserRoleEntity ur on ur.role.roleId = r.roleId " +
            "where ur.user.userId = :userId")
    List<RoleEntity> findAllByUserId(@Param("userId") UUID userId);

    @Query("select distinct r from RoleEntity r " +
            "where r.roleId = :roleId")
    Optional<RoleEntity> findByRoleId(@Param("roleId") UUID roleId);

    @Query("select r from RoleEntity r " +
            "where r.roleId in (:roleIds) " +
            "order by r.code asc")
    List<RoleEntity> findByRoleIds(@Param("roleIds") Collection<UUID> roleIds);


    @Query("select case when count(ur)> 0 then true else false end " +
            "from UserRoleEntity ur " +
            "where ur.role.roleId = :id")
    boolean existsRoleAlreadyUsedByUser(@Param("id") UUID id);

}
