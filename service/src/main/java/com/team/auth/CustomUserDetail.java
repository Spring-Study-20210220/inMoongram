package com.team.auth;

import com.team.user.User;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;


public class CustomUserDetail implements UserDetails {

    private final Long id;
    private final String email;
    private final String name;
    private final String nickName;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    @Builder
    public CustomUserDetail(Long id, String email, String name, String nickName, String password) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.nickName = nickName;
        this.password = password;
        this.authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_USER")
        );
    }

    public static CustomUserDetail create(User user) {
        return CustomUserDetail.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .nickName(user.getNickname())
                .name(user.getName())
                .build();
    }

    public Long getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
