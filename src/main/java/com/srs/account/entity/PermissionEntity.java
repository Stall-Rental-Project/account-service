package com.srs.account.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "permission")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(updatable = false, nullable = false)
    private UUID permissionId;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "permission_category_id", nullable = false)
    private PermissionCategoryEntity permissionCategory;

    @Column(nullable = false)
    private Timestamp createdAt;

    @Column
    private Timestamp updatedAt;

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
