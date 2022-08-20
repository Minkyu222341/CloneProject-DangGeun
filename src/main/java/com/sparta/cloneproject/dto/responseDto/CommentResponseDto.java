package com.sparta.cloneproject.dto.responseDto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentResponseDto {

    private long id;
    private String username;
    private String content;


    @Builder
    public CommentResponseDto(long id, String username, String content) {
        this.id = id;
        this.username = username;
        this.content = content;
    }
}

