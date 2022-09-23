package com.srs.account.repository;

import com.srs.account.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    @Query("select u from UserEntity u " +
            "where lower(u.email) = lower(:email) ")
    Optional<UserEntity> findByEmail(@Param("email") String email);

}
