package com.toyproject.project.domain.member.repository;

import com.toyproject.project.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);


    default Member findByEmailOrElseThrow(String email){
        return findByEmail(email).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }

    default Member findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }

    Optional<Member> findByEmailAndSocialCode(String email, String socialCode);
}
