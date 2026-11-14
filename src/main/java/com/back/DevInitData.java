package com.back;

import com.back.question.Question;
import com.back.question.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

// 개발 환경(dev)에서 페이징을 확인할 수 있도록 질문 300개를 만든다 (교재 3-02).
// Creates 300 questions in the dev environment so paging can be seen (book 3-02).
//
// 교재는 테스트 코드에서 300개를 만들어 개발 DB에 넣지만, 우리 테스트는 메모리 DB를 쓰므로 dev 프로필 전용 초기 데이터로 만든다.
// test 프로필에서는 실행되지 않아, "샘플 질문 2개"를 전제로 한 테스트(findAll 2개 등)가 그대로 유지된다.
// The book creates the 300 from a test into the dev database; our tests use an in-memory database, so this is dev-only startup data instead.
// It never runs in the test profile, so tests that assume "2 sample questions" (findAll returns 2, etc.) keep working.
@Profile("dev")
@Configuration
@RequiredArgsConstructor
public class DevInitData {
    private final QuestionRepository questionRepository;

    @Bean
    // @Order(2): TestInitData(@Order(1)) 가 기본 샘플을 넣은 뒤에 실행.
    // @Order(2): runs after TestInitData (@Order(1)) has inserted the base samples.
    @Order(2)
    public ApplicationRunner devInitDataApplicationRunner() {
        return args -> {
            // 이미 충분히 있으면 다시 만들지 않는다 (앱을 재시작할 때마다 300개씩 늘어나지 않도록).
            // Skip if there's already plenty (so each restart doesn't add another 300).
            if (questionRepository.count() >= 100) return;

            for (int i = 1; i <= 300; i++) {
                Question question = new Question();
                // %03d: 3자리 숫자, 빈 자리는 0 → 001, 002, ...
                // %03d: a 3-digit number padded with zeros → 001, 002, ...
                question.setSubject("테스트 데이터입니다:[%03d]".formatted(i));
                question.setContent("내용무");
                questionRepository.save(question);
            }
        };
    }
}
