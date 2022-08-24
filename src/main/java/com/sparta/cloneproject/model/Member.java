package com.sparta.cloneproject.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String nickname;


    private Long kakaoId;

    @Enumerated(EnumType.STRING)
    private Authority authority;


    @Builder

    public Member(Long id, String username, String password, String nickname, Long kakaoId, Authority authority) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.kakaoId = kakaoId;
        this.authority = authority;
    }
}
