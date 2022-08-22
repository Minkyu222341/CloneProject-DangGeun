package com.sparta.cloneproject.dto.responseDto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserInfoResponseDto {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiresIn;
    private String nickname;
    private String username;

    @Builder
    public UserInfoResponseDto(String grantType, String accessToken, String refreshToken, Long accessTokenExpiresIn, String nickname, String username) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpiresIn = accessTokenExpiresIn;
        this.nickname = nickname;
        this.username = username;
    }
}

