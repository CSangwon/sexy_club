package com.example.chattest.modules.mail.repository;

import com.example.chattest.modules.mail.entity.EmailAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailAuthRepository extends JpaRepository<EmailAuth, Long>, EmailAuthCustomRepository{

    Optional<EmailAuth> findByEmail(String email);

    Boolean existsByEmail(String email);
}
