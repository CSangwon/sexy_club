package com.example.chattest.modules;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseEntity {
    @CreatedDate
    @Column(name = "created_date", nullable = false)
    protected LocalDateTime createdDate;

    @CreatedDate
    @Column(name = "modified_date")
    protected LocalDateTime modifiedDate;
}
