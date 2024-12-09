package com.toyproject.project.domain.chat.repository;

import com.toyproject.project.domain.chat.domain.ChatRoom;
import com.toyproject.project.domain.chat.domain.MemberChatRoom;
import com.toyproject.project.domain.member.entity.Member;
import com.toyproject.project.domain.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
class MemberChatRoomRepositoryTest {

    @Autowired
    private MemberChatRoomRepository memberChatRoomRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    private MemberChatRoom memberChatRoom;
    private Member savedMember1;
    private ChatRoom savedChatRoom1;

    @BeforeEach
    public void setUp() {
        savedMember1 = memberRepository.save(Member.builder()
                .email("test@gmail.com")
                .nickname("test")
                .password("1234")
                .build());

        savedChatRoom1 = chatRoomRepository.save(ChatRoom.builder()
                .name("ChatRoom1")
                .build());

        memberChatRoom = MemberChatRoom.builder()
                .member(savedMember1)
                .chatRoom(savedChatRoom1)
                .build();
    }

    @Test
    @DisplayName("save: 회원 채팅방 연결테이블 저장")
    public void save() {
        // given

        // when
        MemberChatRoom savedMemberChatRoom = memberChatRoomRepository.save(memberChatRoom);

        // then
        Assertions.assertThat(savedMemberChatRoom.getMember()).isNotNull();
        Assertions.assertThat(savedMemberChatRoom.getChatRoom()).isNotNull();
    }

    @Test
    @DisplayName("findByMemberId: 멤버 아이디로 채팅방 조회")
    public void findByMemberId() {
        // given
        memberChatRoomRepository.save(memberChatRoom);

        // when
        List<MemberChatRoom> memberChatRooms = memberChatRoomRepository.findByMemberId(savedMember1.getId());

        // then
        Assertions.assertThat(memberChatRooms.get(0).getMember()).isNotNull();
        Assertions.assertThat(memberChatRooms.get(0).getChatRoom()).isNotNull();
    }


}