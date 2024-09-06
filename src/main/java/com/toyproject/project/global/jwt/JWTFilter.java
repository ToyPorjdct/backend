package com.toyproject.project.global.jwt;

import com.toyproject.project.domain.member.entity.Member;
import com.toyproject.project.domain.member.repository.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter { // OncePerRequestFilter 는 HTTP 요청당 한 번씩만 실행되도록 보장하는 추상 클래스

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("JwtFilter");

        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        try{
            if(authorization == null || !authorization.startsWith("Bearer ")){
                logger.error("Token 이 존재하지 않습니다.");
                filterChain.doFilter(request, response);
                return;
            }

            String token = authorization.split(" ")[1];

            if(jwtTokenProvider.isExpired(token)){
                log.error("Token 이 만료되었습니다.");
                filterChain.doFilter(request, response);
                return;
            }


            String uuid = jwtTokenProvider.getUuid(token);

            Member member = memberRepository.findByUuidOrElseThrow(uuid);

            CustomUserDetails userDetails = new CustomUserDetails(member);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);
        } catch (Exception e){
            log.error("jwt 토큰 검증 오류");
            filterChain.doFilter(request, response);
        }
    }


}