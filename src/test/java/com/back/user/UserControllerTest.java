package com.back.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 회원가입 테스트. 클래스 @Transactional 로 가입한 회원은 테스트가 끝나면 롤백된다.
// Sign-up tests. The class-level @Transactional rolls back any members created here.
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("POST /user/signup: 가입하면 / 로 이동하고, 비밀번호는 암호화되어 저장된다")
    void t1() throws Exception {
        mvc.perform(post("/user/signup").with(csrf())
                        .param("username", "newuser")
                        .param("password1", "1234")
                        .param("password2", "1234")
                        .param("email", "newuser@test.com"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        SiteUser user = userRepository.findByUsername("newuser").orElseThrow();
        // 원래 비밀번호 "1234" 가 그대로 저장되지 않고, BCrypt 형식("$2a$...")으로 저장되어야 한다.
        // The raw password "1234" must not be stored; it must be in BCrypt form ("$2a$...").
        assertThat(user.getPassword()).isNotEqualTo("1234").startsWith("$2a$");
    }

    @Test
    @DisplayName("POST /user/signup: 비밀번호 확인이 다르면 오류 메시지")
    void t2() throws Exception {
        mvc.perform(post("/user/signup").with(csrf())
                        .param("username", "newuser")
                        .param("password1", "1234")
                        .param("password2", "5678")
                        .param("email", "newuser@test.com"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("2개의 패스워드가 일치하지 않습니다.")));
    }

    @Test
    @DisplayName("POST /user/signup: 같은 아이디로 두 번 가입하면 '이미 등록된 사용자입니다.'")
    void t3() throws Exception {
        mvc.perform(post("/user/signup").with(csrf())
                .param("username", "dupuser").param("password1", "1234")
                .param("password2", "1234").param("email", "dup1@test.com"));

        mvc.perform(post("/user/signup").with(csrf())
                        .param("username", "dupuser").param("password1", "1234")
                        .param("password2", "1234").param("email", "dup2@test.com"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("이미 등록된 사용자입니다.")));
    }

    @Test
    @DisplayName("POST /user/login: 샘플 회원 user1/1234 로 로그인하면 / 로 이동하고 인증된다")
    void t4() throws Exception {
        // formLogin(): 시큐리티의 로그인 폼 제출(POST /login 계열)을 흉내 낸다. loginProcessingUrl 을 우리 주소로 지정.
        // formLogin(): simulates submitting Security's login form. We point loginProcessingUrl at our URL.
        mvc.perform(formLogin("/user/login").user("user1").password("1234"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                // authenticated().withUsername(...): 로그인 상태이고 아이디가 이것인가?
                // authenticated().withUsername(...): logged in, with this username?
                .andExpect(authenticated().withUsername("user1"));
    }

    @Test
    @DisplayName("POST /user/login: 비밀번호가 틀리면 /user/login?error 로 이동하고 인증되지 않는다")
    void t5() throws Exception {
        mvc.perform(formLogin("/user/login").user("user1").password("wrong"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/login?error"))
                .andExpect(unauthenticated());
    }
}
