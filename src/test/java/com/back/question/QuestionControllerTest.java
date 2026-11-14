package com.back.question;

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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
                .andExpect(content().string(containsString("href=\"/question/detail/1\"")));
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
}
