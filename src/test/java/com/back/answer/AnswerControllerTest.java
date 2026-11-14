package com.back.answer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
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
    void t1() throws Exception {
        // post(...).param("content", ...): 폼의 textarea(name="content") 에 글을 쓰고 제출한 것과 같다.
        // post(...).param("content", ...): the same as typing into the form's textarea (name="content") and submitting.
        mvc.perform(post("/answer/create/1").param("content", "테스트 답변입니다."))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/question/detail/1"));

        // 상세 화면에 새 답변과 개수가 나오는지 확인
        // Check the detail page shows the new answer and the count
        mvc.perform(get("/question/detail/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("1개의 답변이 있습니다.")))
                .andExpect(content().string(containsString("테스트 답변입니다.")));
    }
}
