package com.toyproject.project.domain.member.repository;

import com.toyproject.project.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    Optional<Member> findByUuid(String uuid);

    default Member findByUuidOrElseThrow(String uuid){
        return findByUuid(uuid).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }

    default Member findByEmailOrElseThrow(String email){
        return findByEmail(email).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }

}
