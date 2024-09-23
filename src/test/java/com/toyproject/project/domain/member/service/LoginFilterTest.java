package com.toyproject.project.domain.member.service;

import com.toyproject.project.domain.member.dto.request.JoinRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
public class LoginFilterTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity()) // Spring Security 통합
                .build();

        JoinRequest joinRequest = JoinRequest.builder()
                .email("test@test.com")
                .nickname("test")
                .password("1234")
                .build();

        authService.join(joinRequest);
    }

    @Test
    @DisplayName("로그인 성공")
    public void login() throws Exception {
        String jsonRequest = "{ \"email\": \"test@test.com\", \"password\": \"1234\" }";

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Authorization")); // Authorization 헤더가 있는지 확인
    }

    @Test
    @DisplayName("로그인 실패 : 비밀번호 틀림")
    public void loginFail() throws Exception {
        String jsonRequest = "{ \"email\": \"test@test.com\", \"password\": \"12345\" }";

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUnauthorized()); // 401 상태 코드 확인

    }

    @Test
    @DisplayName("로그인 실패 : 아이디 틀림")
    public void loginFail2() throws Exception {
        String jsonRequest = "{ \"email\": \"test2@test.com\", \"password\": \"1234\" }";

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUnauthorized()); // 401 상태 코드 확인

    }

    @Test
    @DisplayName("비로그인 상태에서 회원서비스 접근")
    public void accessDenied() throws Exception {
        mockMvc.perform(post("/member/my-info"))
                .andExpect(status().isUnauthorized()); // 401 상태 코드 확인
    }



}
