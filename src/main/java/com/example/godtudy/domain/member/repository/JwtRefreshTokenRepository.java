package com.example.godtudy.domain.member.repository;

import com.example.godtudy.domain.member.entity.JwtRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JwtRefreshTokenRepository extends JpaRepository<JwtRefreshToken, Long> {

    JwtRefreshToken findByUsername(String username);
}
