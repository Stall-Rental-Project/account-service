package com.srs.account.entity;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "role_permission")
@Getter
@Setter
@IdClass(RolePermissionId.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionEntity {

    @Id
    @Column(updatable = false, nullable = false)
    private UUID roleId;

    @Id
    @Column(updatable = false, nullable = false)
    private UUID permissionId;
}
