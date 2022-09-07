package com.srs.account.entity;

import com.srs.common.common.util.TimestampUtil;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID userId;

    private String email;
    private String externalId;
    private String password;
    private String firstName;
    private String middleName;
    private String lastName;

    private int status;

    private String divisions;

    private String marketCodes;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "user")
    private List<UserRoleEntity> userRoles = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.createdAt = TimestampUtil.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = TimestampUtil.now();
    }
}
