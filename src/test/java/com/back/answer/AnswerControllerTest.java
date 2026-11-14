package com.back.answer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
// @WithMockUser: 이 테스트를 "로그인한 사용자"로 실행한다 (실제 로그인 과정 없이). username 은 Principal.getName() 이 된다.
// @WithMockUser: runs the test as a "logged-in user" (without an actual login). username becomes Principal.getName().
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// AnswerController 테스트. 클래스 @Transactional 로 등록한 답변은 테스트가 끝나면 롤백된다.
// Tests for AnswerController. The class-level @Transactional rolls back any answers created here.
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AnswerControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("POST /answer/create/1: 답변 등록 후 상세 화면으로 이동하고, 답변이 보인다")
    @WithMockUser(username = "user1")
    void t1() throws Exception {
        // post(...).param("content", ...): 폼의 textarea(name="content") 에 글을 쓰고 제출한 것과 같다.
        // .with(csrf()): 시큐리티(025-01)가 요구하는 CSRF 토큰을 넣는다. 실제 화면은 th:action 이 자동으로 넣어 준다.
        //               없으면 403 Forbidden 으로 거절된다 (025-01 에서 확인).
        // post(...).param("content", ...): the same as typing into the form's textarea (name="content") and submitting.
        // .with(csrf()): adds the CSRF token Security (025-01) requires. Real pages get it automatically from th:action.
        //               Without it the request is rejected with 403 Forbidden (seen in 025-01).
        mvc.perform(post("/answer/create/1").with(csrf()).param("content", "테스트 답변입니다."))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/question/detail/1#answer_*"));

        // 상세 화면에 새 답변과 개수가 나오는지 확인
        // Check the detail page shows the new answer and the count
        mvc.perform(get("/question/detail/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("1개의 답변이 있습니다.")))
                .andExpect(content().string(containsString("테스트 답변입니다.")));
    }

    @Test
    @DisplayName("POST /answer/create/1: 내용이 비어 있으면 저장하지 않고 오류 메시지를 보여준다")
    @WithMockUser(username = "user1")
    void t2() throws Exception {
        mvc.perform(post("/answer/create/1").with(csrf()).param("content", ""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("내용은 필수항목입니다.")))
                // 저장되지 않았으므로 답변은 여전히 0개
                // Nothing was saved, so there are still 0 answers
                .andExpect(content().string(containsString("0개의 답변이 있습니다.")));
    }

    @Test
    @DisplayName("POST /answer/create/1: 로그인하지 않으면 로그인 화면으로 보낸다 (@PreAuthorize)")
    void t3() throws Exception {
        mvc.perform(post("/answer/create/1").with(csrf()).param("content", "익명 답변"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/login"));
    }

    @Test
    @DisplayName("POST /answer/create/2: 로그인한 사용자(user1)가 답변의 글쓴이로 저장된다")
    @WithMockUser(username = "user1")
    void t4() throws Exception {
        mvc.perform(post("/answer/create/2").with(csrf()).param("content", "글쓴이 확인용 답변"));

        // 2번 질문 상세 화면에 user1 이 글쓴이로 보인다 (샘플 답변도 user1, 질문 글쓴이는 user2)
        // The question 2 detail page shows user1 as an author (the sample answer is also user1; the question's author is user2)
        mvc.perform(get("/question/detail/2"))
                .andExpect(content().string(containsString("글쓴이 확인용 답변")))
                .andExpect(content().string(containsString("user1")))
                .andExpect(content().string(containsString("user2")));
    }

    // ── 답변 수정·삭제 (029-02). 샘플 답변 1번(2번 질문)의 글쓴이는 user1 ─────────────
    // ── Answer edit/delete (029-02). Sample answer 1 (on question 2) is by user1 ─────

    @Test
    @DisplayName("PUT /answer/modify/1: 글쓴이(user1)가 수정하면 2번 질문 상세로 이동하고 바뀐 내용이 보인다")
    @WithMockUser(username = "user1")
    void t5() throws Exception {
        mvc.perform(put("/answer/modify/1").with(csrf()).param("content", "수정된 답변"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/question/detail/2#answer_1"));

        mvc.perform(get("/question/detail/2"))
                .andExpect(content().string(containsString("수정된 답변")));
    }

    @Test
    @DisplayName("GET /answer/modify/1: 다른 사용자(user2)는 403")
    @WithMockUser(username = "user2")
    void t6() throws Exception {
        mvc.perform(get("/answer/modify/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("DELETE /answer/delete/1: 글쓴이가 삭제하면 2번 질문의 답변이 0개가 된다")
    @WithMockUser(username = "user1")
    void t7() throws Exception {
        mvc.perform(delete("/answer/delete/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/question/detail/2"));

        mvc.perform(get("/question/detail/2"))
                .andExpect(content().string(containsString("0개의 답변이 있습니다.")));
    }
}
