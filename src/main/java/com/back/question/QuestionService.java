package com.back.question;

import com.back.global.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// @Service: 비즈니스 로직(실제 처리 규칙)을 담는 "서비스" 클래스라고 스프링에 알린다. 스프링이 객체를 만들어 둔다.
// 컨트롤러 → 서비스 → 리포지터리 순서로 호출한다 (교재 2-09, 14163 34강).
//   컨트롤러: 웹 요청/응답만 담당 (URL, 화면 이름)
//   서비스:   무엇을 어떻게 처리할지 (예: 없으면 404, 저장 전 검사 등)
//   리포지터리: DB 저장·조회만 담당
// 이렇게 나누면 같은 처리를 여러 컨트롤러에서 재사용할 수 있고, 컨트롤러가 DB를 직접 몰라도 된다.
// @Service: tells Spring this is a "service" class holding business logic (the actual processing rules). Spring creates its object.
// Calls flow controller → service → repository (book 2-09, 14163 lecture 34).
//   controller: only web requests/responses (URLs, template names)
//   service:    what to do and how (e.g. 404 if missing, checks before saving)
//   repository: only saving/reading the database
// Splitting it this way lets several controllers reuse the same logic, and controllers don't need to know about the database.
@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    // 질문 전체 목록.
    // All questions.
    public List<Question> getList() {
        return questionRepository.findAll();
    }

    // id 로 질문 하나. 없으면 DataNotFoundException → HTTP 404.
    // orElseThrow(() -> ...): 상자가 비어 있으면 우리가 지정한 예외를 던진다 (교재의 if-isPresent-else 보다 짧다).
    // One question by id. If missing, DataNotFoundException → HTTP 404.
    // orElseThrow(() -> ...): throws the exception we choose when the box is empty (shorter than the book's if-isPresent-else).
    public Question getQuestion(int id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("question not found"));
    }

    // 질문 등록. 작성일시는 JPA Auditing 이 자동으로 넣는다 (010-02).
    // @Transactional: 저장 작업을 트랜잭션으로 묶어 끝나면 커밋한다.
    // Creates a question. JPA Auditing fills in the created date automatically (010-02).
    // @Transactional: wraps the save in a transaction and commits at the end.
    @Transactional
    public Question create(String subject, String content) {
        Question question = new Question();
        question.setSubject(subject);
        question.setContent(content);
        return questionRepository.save(question);
    }
}
