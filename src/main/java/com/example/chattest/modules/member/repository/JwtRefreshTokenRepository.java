package com.example.chattest.modules.member.repository;

import com.example.chattest.modules.member.entity.JwtRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtRefreshTokenRepository extends JpaRepository<JwtRefreshToken, Long> {

    JwtRefreshToken findByEmail(String email);
}
