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
import java.util.UUID;

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

    public ChatRoom(String name) {
        this.id = Long.parseLong(UUID.randomUUID().toString());
        this.name = name;
        this.count = 0;
        this.expireDate = LocalDateTime.now().plusHours(VALID_HOUR);
    }

    public boolean isRemovable(){
        if (this.expireDate.isAfter(LocalDateTime.now()) && this.count == 0) {
            return false;
        }
        return true;
    }

    public void enter(){
        this.count += 1;
    }

    public void exit() {
        this.count -= 1;
        if (count < 0) {
            count = 0;
        }
    }
}
