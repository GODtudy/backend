package com.example.godtudy.global.config.security.member;

import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class MemberDetails implements UserDetails {

    private String username;
    private String password;
    List<GrantedAuthority> authorities;

    @Builder
    public MemberDetails(String username, String password, List<GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
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
        return username;
    }

    //계정 만료 여부 -> true(만료되지 않음)
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    //계정 잠김 여부 -> true(잠기지 않음)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //비밀번호 만료 여부 -> true(만료되지 않음)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정이 활성화 된건지 -> true(활성화)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
