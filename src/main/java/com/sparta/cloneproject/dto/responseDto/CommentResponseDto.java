package com.sparta.cloneproject.dto.responseDto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentResponseDto {

    private long id;
    private String content;
    private String nickname;


    @Builder
    public CommentResponseDto(long id, String content,String nickname) {
        this.id = id;
        this.content = content;
        this.nickname = nickname;
    }
}

