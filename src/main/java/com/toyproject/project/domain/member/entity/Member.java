package com.toyproject.project.domain.member.entity;

import com.toyproject.project.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Builder
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 50, nullable = false)
    private String nickname;

    @Column(length = 255, nullable = false)
    private String password;

    @Column(length = 255)
    private String socialCode;

    private boolean status;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Builder.Default
    private String profileImage = "https://github.com/user-attachments/assets/dad652d0-41b9-4f6a-87b8-e1e28ba57e4a";

    public void updateSocialCode(String socialCode) {
        this.socialCode = socialCode;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}
