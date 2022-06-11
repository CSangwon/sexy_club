package com.example.chattest.modules.mail.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailAuth {

    private final static Long MAX_EXPIRE_TIME = 5L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String authToken;

    private Boolean expired;

    private LocalDateTime expireTime;

    public void setToken(){
        this.expired = true;
    }

    public void setTokenExpireTime(){
        this.expireTime = LocalDateTime.now().plusMinutes(MAX_EXPIRE_TIME);
    }
}
