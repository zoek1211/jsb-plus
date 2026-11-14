package com.back.question;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// QuestionController 의 화면(HTTP 응답)을 검사한다. MockMvc 사용법은 MainControllerTest 참고.
// Tests QuestionController's pages (HTTP responses). See MainControllerTest for how MockMvc works.
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
// 각 테스트가 끝나면 롤백 → 글 등록 같은 테스트가 다른 테스트에 영향을 주지 않는다.
// Roll back after each test → tests that create data don't affect other tests.
@Transactional
class QuestionControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("GET /question/list: 샘플 질문 제목들이 화면에 보인다")
    void t1() throws Exception {
        mvc.perform(get("/question/list"))
                .andDo(print())
                .andExpect(status().isOk())
                // containsString: 응답 본문(HTML)에 이 글자가 들어 있는가?
                // containsString: does the response body (HTML) contain this text?
                .andExpect(content().string(containsString("sbb가 무엇인가요?")))
                .andExpect(content().string(containsString("스프링부트 모델 질문입니다.")))
                // 목록의 제목이 상세 화면 링크인지 확인
                // Check the subjects link to the detail page
                .andExpect(content().string(containsString("href=\"/question/detail/1\"")))
                // 레이아웃의 title-pattern 이 적용되었는지 확인 (017-01)
                // Check the layout's title-pattern was applied (017-01)
                .andExpect(content().string(containsString("<title>질문 목록 | SBB</title>")));
    }

    @Test
    @DisplayName("GET /question/list: 게시물 번호가 전체 개수부터 거꾸로 매겨진다 (2, 1)")
    void t1_numbering() throws Exception {
        mvc.perform(get("/question/list"))
                .andExpect(status().isOk())
                // 첫 줄(최신 질문)의 번호는 전체 개수 2
                // The first row (newest question) is numbered with the total count, 2
                .andExpect(content().string(containsString("<td>2</td>")))
                .andExpect(content().string(containsString("<td>1</td>")));
    }

    @Test
    @DisplayName("GET /question/list: 답변이 있는 질문(2번, 답변 1개) 옆에 답변 개수가 보인다")
    void t1_answerCount() throws Exception {
        mvc.perform(get("/question/list"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("text-danger small ms-2")));
    }

    @Test
    @DisplayName("GET /question/list?kw=sbb: 검색 결과에 1번만 보이고, 검색창에 검색어가 남는다 (033-01)")
    void t1_search() throws Exception {
        mvc.perform(get("/question/list").param("kw", "sbb"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("sbb가 무엇인가요?")))
                .andExpect(content().string(org.hamcrest.Matchers.not(containsString("스프링부트 모델 질문입니다."))))
                .andExpect(content().string(containsString("value=\"sbb\"")));
    }

    @Test
    @DisplayName("GET /question/detail/1: 1번 질문의 제목과 내용이 보인다")
    void t2() throws Exception {
        mvc.perform(get("/question/detail/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("sbb가 무엇인가요?")))
                .andExpect(content().string(containsString("sbb에 대해서 알고 싶습니다.")));
    }

    @Test
    @DisplayName("GET /question/detail/999: 없는 질문이면 404")
    void t3() throws Exception {
        mvc.perform(get("/question/detail/999"))
                .andDo(print())
                // isNotFound(): HTTP 상태 코드가 404 인가? (DataNotFoundException 의 @ResponseStatus)
                // isNotFound(): is the HTTP status 404? (from DataNotFoundException's @ResponseStatus)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /question/create: 로그인하지 않으면 로그인 화면으로 보낸다 (@PreAuthorize)")
    void t4_anonymous() throws Exception {
        mvc.perform(get("/question/create"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/login"));
    }

    @Test
    @DisplayName("GET /question/create: 질문 등록 폼이 보인다")
    @WithMockUser(username = "user1")
    void t4() throws Exception {
        mvc.perform(get("/question/create"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("질문등록")));
    }

    @Test
    @DisplayName("POST /question/create: 질문 등록 후 목록으로 이동하고, 새 질문이 보인다")
    @WithMockUser(username = "user1")
    void t5() throws Exception {
        mvc.perform(post("/question/create").with(csrf())
                        .param("subject", "새 질문 제목")
                        .param("content", "새 질문 내용"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/question/list"));

        mvc.perform(get("/question/list"))
                .andExpect(content().string(containsString("새 질문 제목")));
    }

    @Test
    @DisplayName("POST /question/create: 제목·내용이 비어 있으면 저장하지 않고 오류 메시지와 함께 폼을 다시 보여준다")
    @WithMockUser(username = "user1")
    void t6() throws Exception {
        mvc.perform(post("/question/create").with(csrf())
                        .param("subject", "")
                        .param("content", "내용만 있음"))
                .andDo(print())
                // 리다이렉트가 아니라 200 + 폼 화면
                // Not a redirect: 200 with the form page
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("제목은 필수항목입니다.")))
                // 입력했던 내용은 그대로 남아 있어야 한다
                // The content the user typed must still be there
                .andExpect(content().string(containsString("내용만 있음")));
    }

    // ── 수정·삭제 (029-01). 1번 질문의 글쓴이는 user1 ─────────────────────────────
    // ── Edit/delete (029-01). Question 1's author is user1 ─────────────────────────

    @Test
    @DisplayName("GET /question/modify/1: 글쓴이(user1)는 기존 내용이 채워진 수정 폼을 본다")
    @WithMockUser(username = "user1")
    void t7() throws Exception {
        mvc.perform(get("/question/modify/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("질문수정")))
                .andExpect(content().string(containsString("sbb가 무엇인가요?")))
                // th:method="put" 이 만든 숨은 필드 / The hidden field made by th:method="put"
                .andExpect(content().string(containsString("name=\"_method\" value=\"put\"")));
    }

    @Test
    @DisplayName("GET /question/modify/1: 다른 사용자(user2)는 403")
    @WithMockUser(username = "user2")
    void t8() throws Exception {
        mvc.perform(get("/question/modify/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("PUT /question/modify/1: 글쓴이가 수정하면 상세 화면에 바뀐 제목이 보인다")
    @WithMockUser(username = "user1")
    void t9() throws Exception {
        // put(...): HTTP PUT 요청. 실제 화면에서는 POST + _method=put 이 필터를 거쳐 PUT 이 된다.
        // put(...): an HTTP PUT request. In the real page, POST + _method=put becomes PUT through the filter.
        mvc.perform(put("/question/modify/1").with(csrf())
                        .param("subject", "수정된 제목")
                        .param("content", "수정된 내용"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/question/detail/1"));

        mvc.perform(get("/question/detail/1"))
                .andExpect(content().string(containsString("수정된 제목")));
    }

    @Test
    @DisplayName("POST + _method=delete /question/delete/2: 글쓴이(user2)가 삭제하면 / 로 이동하고 상세는 404")
    @WithMockUser(username = "user2")
    void t10() throws Exception {
        // 화면과 똑같이 POST + 숨은 _method=delete 로 보낸다 → HiddenHttpMethodFilter 가 DELETE 로 바꾼다.
        // Send POST + hidden _method=delete just like the page → HiddenHttpMethodFilter turns it into DELETE.
        mvc.perform(post("/question/delete/2").with(csrf()).param("_method", "delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        mvc.perform(get("/question/detail/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /question/vote/1: 같은 사람이 두 번 추천해도 추천 수는 1 (Set, 030-01)")
    @WithMockUser(username = "user2")
    void t12() throws Exception {
        mvc.perform(post("/question/vote/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/question/detail/1"));
        mvc.perform(post("/question/vote/1").with(csrf()));

        // 추천 배지의 숫자가 1 / The recommend badge shows 1
        mvc.perform(get("/question/detail/1"))
                .andExpect(content().string(containsString("badge rounded-pill bg-success\">1</span>")));
    }

    @Test
    @DisplayName("POST /answer/vote/1: 답변 추천 후 추천 수 1 (030-01)")
    @WithMockUser(username = "user2")
    void t13() throws Exception {
        mvc.perform(post("/answer/vote/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/question/detail/2#answer_1"));

        mvc.perform(get("/question/detail/2"))
                .andExpect(content().string(containsString("badge rounded-pill bg-success\">1</span>")));
    }

    @Test
    @DisplayName("DELETE /question/delete/2: 글쓴이가 아닌 user1 은 403")
    @WithMockUser(username = "user1")
    void t11() throws Exception {
        mvc.perform(delete("/question/delete/2").with(csrf()))
                .andExpect(status().isForbidden());
    }
}
