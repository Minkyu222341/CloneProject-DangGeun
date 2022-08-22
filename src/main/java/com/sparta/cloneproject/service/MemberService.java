package com.sparta.cloneproject.service;

import com.sparta.cloneproject.dto.requestDto.MemberRequestDto;
import com.sparta.cloneproject.dto.responseDto.MemberResponseDto;
import com.sparta.cloneproject.dto.responseDto.TokenDto;
import com.sparta.cloneproject.jwt.TokenProvider;
import com.sparta.cloneproject.model.Member;
import com.sparta.cloneproject.model.RefreshToken;
import com.sparta.cloneproject.repository.MemberRepository;
import com.sparta.cloneproject.repository.RefreshTokenRepository;
import com.sparta.cloneproject.sercurity.CustomerUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CustomerUserDetailsService userDetailsService;

    public String getLoginMemberNickname() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Member> member = memberRepository.findById(Long.valueOf(userId));
        return member.get().getNickname();
    }

    @Transactional
    public MemberResponseDto signup(MemberRequestDto memberRequestDto) {
        if (!(Pattern.matches("^[a-zA-Z](?=.{0,28}[0-9])[0-9a-zA-Z]{4,15}$", memberRequestDto.getUsername()) && (memberRequestDto.getUsername().length() > 3 && memberRequestDto.getUsername().length() < 13)
                && Pattern.matches("^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,25}$", memberRequestDto.getPassword()) && (memberRequestDto.getPassword().length() > 3 && memberRequestDto.getPassword().length() < 33))) {
            throw new IllegalArgumentException("닉네임 혹은 비밀번호 조건을 확인해주세요.");
        }

        Member member = memberRequestDto.toMember(passwordEncoder);
        return MemberResponseDto.of(memberRepository.save(member));
    }

    @Transactional
    public TokenDto login(MemberRequestDto memberRequestDto) {
        if (!memberRepository.existsByUsername(memberRequestDto.getUsername())) {
            throw new RuntimeException("사용자를 찾을 수 없습니다");
        }
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto.toAuthentication();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        try{
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);


            // 3. 인증 정보를 기반으로 JWT 토큰 생성
            TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

            // 4. RefreshToken 저장
            RefreshToken refreshToken = RefreshToken.builder()
                    .key(authentication.getName())
                    .value(tokenDto.getRefreshToken())
                    .build();

            refreshTokenRepository.save(refreshToken);


            // 5. 토큰 발급
            return tokenDto;
        } catch (Exception e){
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }
    }


    public boolean validateUsername(MemberRequestDto memberRequestDto) {
        boolean flag = true;
        if (memberRepository.existsByUsername(memberRequestDto.getUsername())) {
            flag = false;
        }
        return flag;
    }

    public boolean validateNickname(MemberRequestDto memberRequestDto) {
        boolean flag = true;
        if (memberRepository.existsByNickname(memberRequestDto.getNickname())) {
            flag = false;
        }
        return flag;
    }

    public String getLoginNickname() {
        return getLoginMemberNickname();
    }


//    @Transactional
//    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
//        // 1. Refresh Token 검증
//        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
//            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
//        }
//
//        // 2. Access Token 에서 Member ID 가져오기
//        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());
//
//        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
//        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
//                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));
//
//        // 4. Refresh Token 일치하는지 검사
//        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
//            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
//        }
//
//        // 5. 새로운 토큰 생성
//        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
//
//        // 6. 저장소 정보 업데이트
//        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
//        refreshTokenRepository.save(newRefreshToken);
//
//        // 토큰 발급
//        return tokenDto;
//    }
}