package com.sparta.cloneproject.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.cloneproject.dto.requestDto.KakaoUserInfoDto;
import com.sparta.cloneproject.dto.responseDto.TokenDto;
import com.sparta.cloneproject.dto.responseDto.UserInfoResponseDto;
import com.sparta.cloneproject.jwt.TokenProvider;
import com.sparta.cloneproject.model.Member;
import com.sparta.cloneproject.repository.MemberRepository;
import com.sparta.cloneproject.sercurity.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoUserService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository userRepository;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;


    public UserInfoResponseDto kakaoLogin(String code) throws JsonProcessingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청

        String accessToken = getAccessToken(code);
        System.out.println(accessToken+" 유저요청엑세스토큰");
        // 2. 토큰으로 카카오 API 호출
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);
        System.out.println(kakaoUserInfo+" 유저정보");
        // 3. 필요시에 회원가입
        Member kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);
        System.out.println(kakaoUser + " 유저정보2");

        // 4. 강제 로그인 처리
        UserInfoResponseDto tokenDto = forceLogin(kakaoUser);

        return tokenDto;
    }

    private String getAccessToken(String code) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        log.info("catch code={}",code);
        System.out.println(code);
        // HTTP Body 생성
        //받은 코드를 헤더랑 바디에 세팅해서 http메세지로 만들어서 카카오로 보낸다음에 ㅔ우리가 그 토큰을 받아서 어떻게 핸들링한다 ㅣㅇ거여
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "f072c106f2f26c3921bee727b2df0ccd");
//        body.add("redirect_uri", "http://www.sparta99.com/user/kakao/callback");
        body.add("redirect_uri", "http://localhost:3000/user/kakao/callback");
        body.add("code", code);
        /**
         * 받은 인가코드로 카카오에 엑세스토큰 요청
         */
        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        /**
         * 토큰 파싱해서 리턴
         */
        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        log.info("catch token={}",jsonNode.get("access_token"));
        return jsonNode.get("access_token").asText();
    }

    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        /**
         * 리턴받은 토큰으로 카카오에 유저정보 요청
         */
        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );
        /**
         * 유저정보 파싱해서 리턴
         */
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);


        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
        String email = jsonNode.get("kakao_account")
                .get("email").asText();

        System.out.println("카카오 사용자 정보: " + id + ", " + nickname + ", " + email);
        return new KakaoUserInfoDto(id, nickname, email);
    }

    private Member registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
        /**
         *  응답받은 유저정보를 토대로 나의 DB에 같은 유저가 가입되어 있는지 확인 후 없다면 DB에 정보 저장 아니면 그대로 리턴
         */
        // DB 에 중복된 Kakao Id 가 있는지 확인
        Long id = kakaoUserInfo.getId();
        Member member = userRepository.findByKakaoId(id);
        if (member == null) {
            // 회원가입
            // username: kakao nickname
            String username = kakaoUserInfo.getNickname();

            // password: random UUID
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);

            // email: kakao email
            String email = kakaoUserInfo.getEmail();
            Member signUp = Member.builder()
                    .kakaoId(id)
                    .username(email)
                    .nickname(username)
                    .password(encodedPassword)
                    .build();
            Member signUpMember = userRepository.save(signUp);
            return signUpMember;
        }
        return member;
    }

    private UserInfoResponseDto forceLogin(Member kakaoUser) {
        /**
         *  가입된 유저의 정보를 받아서 authentication 객체를 생성해서 프론트와 통신할 유효한 토큰을 생성하고 SecurityContextHolder에
         *  authentication 객체를 SET 해서 강제로 로그인처리.
         */
        UserDetails userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        Optional<Member> loginMember = memberRepository.findByUsername(kakaoUser.getUsername());
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserInfoResponseDto tokenAndUserInfo = UserInfoResponseDto.builder()
                .accessToken(tokenDto.getAccessToken())
                .accessTokenExpiresIn(tokenDto.getAccessTokenExpiresIn())
                .grantType(tokenDto.getGrantType())
                .refreshToken(tokenDto.getRefreshToken())
                .username(loginMember.get().getUsername())
                .nickname(loginMember.get().getNickname())
                .build();
        return tokenAndUserInfo;
    }
}