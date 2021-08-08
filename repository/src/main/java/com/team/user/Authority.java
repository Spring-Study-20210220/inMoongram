package com.team.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public Authority(Long id, UserRole role) {
        this.id = id;
        this.role = role;
    }

    public void updateRole(UserRole userRole) {
        this.role = userRole;
    }
}
