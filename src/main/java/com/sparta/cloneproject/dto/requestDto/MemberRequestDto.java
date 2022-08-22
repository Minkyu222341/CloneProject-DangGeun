package com.sparta.cloneproject.dto.requestDto;

import com.sparta.cloneproject.model.Authority;
import com.sparta.cloneproject.model.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto {
//
   private String username;

   private String nickname;

   private String password;

   private String validPassword;

   private Member member;

   public Member toMember(PasswordEncoder passwordEncoder) {
      return Member.builder()
              .username(username)
              .password(passwordEncoder.encode(password))
              .nickname(nickname)
              .authority(Authority.ROLE_USER)
              .build();
   }



   public UsernamePasswordAuthenticationToken toAuthentication() {
      return new UsernamePasswordAuthenticationToken(username, password);
   }
}

