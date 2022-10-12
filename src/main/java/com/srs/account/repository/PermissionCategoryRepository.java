package com.srs.account.repository;

import com.srs.account.entity.PermissionCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository

public interface PermissionCategoryRepository extends JpaRepository<PermissionCategoryEntity, UUID> {
    @Query("select distinct pc from PermissionCategoryEntity pc " +
            "join fetch pc.permissions " +
            "order by pc.name asc")
    List<PermissionCategoryEntity> findAllPermissionCategories();
}
