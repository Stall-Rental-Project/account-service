package com.srs.account.entity;

import com.srs.common.common.util.TimestampUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * @author duynt on 2/17/22
 */
@Getter
@Setter
@Entity
@Table(name = "user_role")
@NoArgsConstructor
public class UserRoleEntity implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID userRoleId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity role;

    private OffsetDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = TimestampUtil.now();
    }

    private UserRoleEntity(UserEntity user, RoleEntity role) {
        this.user = user;
        this.role = role;
    }

    public static UserRoleEntity from(UserEntity user, RoleEntity role) {
        return new UserRoleEntity(user, role);
    }
}
