package com.srs.account.repository;

import com.srs.account.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    @Query("select u from UserEntity u " +
            "where lower(u.email) = lower(:email) ")
    Optional<UserEntity> findByEmail(@Param("email") String email);


    @Query("select u from UserEntity u " +
            "where lower(u.email) like concat('%', lower(:email), '%') " +
            "and exists (select 1 from UserRoleEntity ur " +
            "            inner join RoleEntity r on ur.role.roleId = r.roleId " +
            "            where ur.user.userId = u.userId " +
            "            and r.code = 'PUBLIC_USERS') " +
            "order by u.firstName, u.lastName")
    List<UserEntity> findAllPublicUsersByEmail(@Param("email") String email);
}
