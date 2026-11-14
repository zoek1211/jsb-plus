package com.back.answer;

import com.back.global.exception.DataNotFoundException;
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

    // id 로 답변 하나. 없으면 404 (029-02).
    // One answer by id. 404 if missing (029-02).
    public Answer getAnswer(int id) {
        return answerRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("answer not found"));
    }

    // 답변 수정 (교재 3-09). 수정일시는 JPA Auditing 이 갱신.
    // Edits an answer (book 3-09). JPA Auditing updates the modified date.
    @Transactional
    public void modify(Answer answer, String content) {
        answer.setContent(content);
        answerRepository.save(answer);
    }

    // 답변 삭제 (교재 3-09).
    // 질문의 answers 목록에서도 빼 준다. Question.answers 에 cascade = PERSIST 가 있어서, 목록에 남아 있으면
    // 같은 트랜잭션 안에서 "다시 저장하라"는 신호가 되어 삭제와 충돌할 수 있다 (연관관계 양쪽을 함께 맞추기, 009-02 와 같은 원리).
    // Deletes an answer (book 3-09).
    // Also removes it from the question's answers list. Question.answers has cascade = PERSIST, so leaving it in the list
    // could signal "save this again" within the same transaction and clash with the delete (keeping both sides in sync, as in 009-02).
    @Transactional
    public void delete(Answer answer) {
        answer.getQuestion().getAnswers().remove(answer);
        answerRepository.delete(answer);
    }
}
