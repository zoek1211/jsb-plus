package com.back.answer;

import com.back.question.Question;
import com.back.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 답변 관련 처리 규칙을 담는 서비스 (교재 2-12).
// The service holding the rules for answers (book 2-12).
@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;

    // 질문에 답변을 하나 등록한다.
    // @Transactional: 이 메서드 안의 DB 작업을 하나로 묶어, 끝나면 커밋한다 (테스트가 아니므로 롤백이 아니라 커밋).
    // Registers one answer on a question.
    // @Transactional: groups this method's database work and commits at the end (not a test, so it commits instead of rolling back).
    @Transactional
    // author: 답변 글쓴이 (로그인한 회원, 028-01)
    // author: the answer's writer (the logged-in member, 028-01)
    public Answer create(Question question, String content, SiteUser author) {
        // addAnswer: 연관관계 양쪽(answer.question, question.answers)을 함께 맞춘다 (009-02).
        // addAnswer: keeps both sides of the relationship (answer.question, question.answers) in sync (009-02).
        Answer answer = question.addAnswer(content);
        answer.setAuthor(author);
        // 저장 → INSERT INTO answer ... (question_id, author_id 포함)
        // Save → INSERT INTO answer ... (including question_id and author_id)
        return answerRepository.save(answer);
    }
}
