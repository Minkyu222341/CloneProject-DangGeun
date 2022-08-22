package com.sparta.cloneproject.repository;

import com.sparta.cloneproject.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByUsername(String username);
    Member findByKakaoId(Long id);
    boolean existsByUsername(String username);

    boolean existsByNickname(String nickname);
}
