package com.mbi.study.repository.entity;

import com.mbi.study.common.UserRoleEnum;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userIdGenerator")
    @SequenceGenerator(name = "userIdGenerator", sequenceName = "user_id_seq", allocationSize = 1)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;

    @Column(name = "role_name")
    @Enumerated(EnumType.STRING)
    private UserRoleEnum roleName;
}

