package com.example.chattest.modules.member;

import com.example.chattest.modules.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private Boolean emailVerified;;

    private LocalDate emailCheckTokenGeneratedAt;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String studentIdCard;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

}
