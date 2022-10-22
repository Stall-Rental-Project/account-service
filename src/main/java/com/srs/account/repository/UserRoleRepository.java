package com.srs.account.repository;

import com.srs.account.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.UUID;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, UUID> {
    @Modifying
    @Query("delete from UserRoleEntity ur " +
            "where ur.user.userId = :userId " +
            "and ur.role.roleId in (:roleIds)")
    void deleteAllByUserIdAndRoleIds(@Param("userId") UUID userId, @Param("roleIds") Collection<UUID> roleIds);

    @Modifying
    @Query("delete from UserRoleEntity ur " +
            "where ur.user.userId = :userId ")
    void deleteByUserId(@Param("userId") UUID userId);
}
