package com.srs.account.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "permission_category")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionCategoryEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(updatable = false, nullable = false)
    private UUID permissionCategoryId;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private Timestamp createdAt;

    @Column
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "permissionCategory",fetch = FetchType.LAZY)
    private List<PermissionEntity> permissions;

    @PreUpdate
    public void preUpdate(){
        this.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
    }

    @PrePersist
    public void prePersist(){
        this.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        this.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
    }
}
