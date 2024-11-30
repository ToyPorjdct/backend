package com.toyproject.project.domain.chat.filter;

import com.toyproject.project.domain.member.entity.Member;
import com.toyproject.project.domain.member.repository.MemberRepository;
import com.toyproject.project.global.jwt.JwtTokenProvider;
import com.toyproject.project.global.login.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilterChannelInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    // websocket을 통해 들어온 요청이 처리 되기전 실행됨
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("ChatHandler");
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && accessor.getNativeHeader("Authorization") != null) {
            String header = accessor.getFirstNativeHeader("Authorization");
            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7);
                try {
                    Long memberId = Long.parseLong(jwtTokenProvider.getMemberId(token));
                    Member member = memberRepository.findByIdOrElseThrow(memberId);
                    CustomUserDetails userDetails = new CustomUserDetails(member);

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    accessor.setUser(authToken);
                } catch (Exception e) {
                    log.error("인증 실패: {}", e.getMessage());
                    throw new RuntimeException("Invalid WebSocket token");
                }
            } else {
                log.error("Authorization 헤더가 유효하지 않습니다.");
                throw new RuntimeException("Missing or invalid Authorization header");
            }
        }


        return message;
    }
}
