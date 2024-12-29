package com.toyproject.project.domain.chat.filter;


import com.toyproject.project.global.exception.CustomException;
import com.toyproject.project.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import static com.toyproject.project.global.exception.ErrorCode.NO_AUTHORITY;
import static com.toyproject.project.global.exception.ErrorCode.TOKEN_EXPIRED;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilterChannelInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;


    /**
     * websocket은 http 요청이 아니므로 기존의 jwt filter를 통해 인증처리가 되지 않음
     * 그래서 별도로 websocket을 통해 들어온 요청을 처리하기 위한 인터셉터를 작성
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("ChatHandler");

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && accessor.getNativeHeader("Authorization") != null) {
            String header = accessor.getFirstNativeHeader("Authorization");

            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7);

                try {
                    if (!jwtTokenProvider.validateToken(token)) {
                        throw new CustomException(NO_AUTHORITY);
                    }

                    if (jwtTokenProvider.isExpired(token)) {
                        throw new CustomException(TOKEN_EXPIRED);
                    }

                } catch (Exception e) {
                    log.error("WebSocket 인증 실패: {}", e.getMessage());
                    throw new CustomException(NO_AUTHORITY);
                }
            } else {
                log.error("Authorization 헤더가 유효하지 않습니다.");
                throw new CustomException(NO_AUTHORITY);
            }
        }
        return message;
    }
}
