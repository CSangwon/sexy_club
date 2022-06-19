package com.example.chattest.modules.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom {

    private static final int VALID_HOUR = 3;

    @Id
    private Long id;

    @NotNull
    @Column(length = 23)
    private String name;

    private int count;

    @NotNull
    private LocalDateTime expireDate;
}
