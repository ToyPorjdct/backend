package com.toyproject.project.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;
    @Value("${jwt.expiration-time}")
    private Long EXPIRATION_TIME;

    /**
     * 토큰 생성
     * @param memberId 사용자 식별 번호
     * @return 토큰
     */
    public String createToken(Long memberId) {
        Claims claims = Jwts.claims().setSubject(memberId.toString()); // 토큰에 들어갈 정보
        Date now = new Date();
        Date validity = new Date(now.getTime() + EXPIRATION_TIME);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * 토큰 파싱
     * @param token 토큰
     * @return uuid
     */
    public String getMemberId(String token) {
        token = token.replace("Bearer ", "");
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }


    /**
     * 토큰 유효성 검사
     * @param token 토큰
     * @return 유효성 여부
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public boolean isExpired(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }


}