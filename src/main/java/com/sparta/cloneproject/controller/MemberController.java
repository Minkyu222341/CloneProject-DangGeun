package com.sparta.cloneproject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.cloneproject.dto.requestDto.MemberRequestDto;
import com.sparta.cloneproject.dto.responseDto.MemberResponseDto;
import com.sparta.cloneproject.dto.responseDto.TokenDto;
import com.sparta.cloneproject.model.Member;
import com.sparta.cloneproject.repository.MemberRepository;
import com.sparta.cloneproject.service.KakaoUserService;
import com.sparta.cloneproject.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final KakaoUserService kakaoUserService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;


    @GetMapping("/user/kakao/callback")
    public TokenDto kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        TokenDto tokenDto = kakaoUserService.kakaoLogin(code);
        response.setHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.setHeader("Access-Token-Expire-Time", String.valueOf(tokenDto.getAccessTokenExpiresIn()));
        System.out.println(tokenDto.getAccessToken() + " JWT엑세스토큰 ");
        return tokenDto;
    }

    @PostMapping("/api/member/signup")
    public ResponseEntity<MemberResponseDto> signup(@RequestBody MemberRequestDto memberRequestDto ) {
        return ResponseEntity.ok(memberService.signup(memberRequestDto));
    }
//
    @PostMapping("/api/member/login")
    public Optional<Member> login(@RequestBody MemberRequestDto memberRequestDto, HttpServletResponse response) {
        TokenDto tokenDto = memberService.login(memberRequestDto);
        response.setHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.setHeader("Refresh-Token", tokenDto.getRefreshToken());
        response.setHeader("Access-Token-Expire-Time", String.valueOf(tokenDto.getAccessTokenExpiresIn()));
        return memberRepository.findByUsername(memberRequestDto.getUsername());
    }
}
