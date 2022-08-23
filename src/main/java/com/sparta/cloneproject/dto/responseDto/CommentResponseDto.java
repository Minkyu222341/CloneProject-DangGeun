package com.sparta.cloneproject.dto.responseDto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentResponseDto {

    private long id;
    private String content;
    private String nickname;
    private String username;
    private String creatAt;


    @Builder
    public CommentResponseDto(long id, String content,String nickname,String username,String creatAt) {
        this.id = id;
        this.content = content;
        this.nickname = nickname;
        this.username = username;
        this.creatAt = creatAt;
    }
}

