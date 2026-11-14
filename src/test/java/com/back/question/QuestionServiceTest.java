package com.back.question;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

// QuestionService 의 처리 규칙을 검사한다. 테스트 데이터: 샘플 질문 2개 (TestInitData).
// Tests QuestionService's rules. Test data: the 2 sample questions (TestInitData).
@ActiveProfiles("test")
@SpringBootTest
@Transactional
class QuestionServiceTest {
    @Autowired
    private QuestionService questionService;

    @Test
    @DisplayName("getList(0, \"\"): 첫 페이지에 질문이 최신순(id 내림차순)으로 담긴다")
    void t1() {
        Page<Question> paging = questionService.getList(0, "");

        // getContent(): 이번 페이지의 질문 목록 / getTotalElements(): 전체 개수 / getTotalPages(): 전체 페이지 수
        // getContent(): this page's questions / getTotalElements(): total count / getTotalPages(): total pages
        assertThat(paging.getContent()).hasSize(2);
        assertThat(paging.getTotalElements()).isEqualTo(2);
        assertThat(paging.getTotalPages()).isEqualTo(1);
        // 최신(나중에 저장된) 2번 질문이 먼저
        // The newest (saved later) question 2 comes first
        assertThat(paging.getContent().get(0).getId()).isEqualTo(2);
    }

    @Test
    @DisplayName("getList(1, \"\"): 질문이 2개뿐이면 두 번째 페이지는 비어 있다")
    void t2() {
        Page<Question> paging = questionService.getList(1, "");

        assertThat(paging.getContent()).isEmpty();
        assertThat(paging.hasPrevious()).isTrue();
    }

    // ── 검색 (033-01). 샘플: 1번 "sbb가 무엇인가요?"(user1), 2번 "스프링부트 모델 질문입니다."(user2, 답변 "네 자동으로 생성됩니다." by user1)
    // ── Search (033-01). Samples: #1 "sbb가 무엇인가요?" (user1), #2 "스프링부트 모델 질문입니다." (user2, answer "네 자동으로 생성됩니다." by user1)

    @Test
    @DisplayName("검색: 제목에 'sbb' 가 들어간 질문은 1번 하나")
    void t3() {
        Page<Question> paging = questionService.getList(0, "sbb");

        assertThat(paging.getContent()).hasSize(1);
        assertThat(paging.getContent().get(0).getId()).isEqualTo(1);
    }

    @Test
    @DisplayName("검색: 질문 글쓴이 'user2' 로 찾으면 2번")
    void t4() {
        Page<Question> paging = questionService.getList(0, "user2");

        assertThat(paging.getContent()).extracting(Question::getId).containsExactly(2);
    }

    @Test
    @DisplayName("검색: 답변 내용 '자동으로' 로 찾으면 그 답변이 달린 2번 (중복 없이 1개)")
    void t5() {
        Page<Question> paging = questionService.getList(0, "자동으로");

        // 2번 질문 본문("id는 자동으로 생성되나요?")과 답변 둘 다 걸리지만 distinct 로 한 번만
        // Both question 2's body ("id는 자동으로 생성되나요?") and its answer match, but distinct keeps it once
        assertThat(paging.getContent()).extracting(Question::getId).containsExactly(2);
        assertThat(paging.getTotalElements()).isEqualTo(1);
    }
}
