package com.back.question;

import com.back.global.exception.DataNotFoundException;
import com.back.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // 질문 목록의 한 페이지 (교재 3-02).
    // page: 0부터 시작하는 페이지 번호. 한 페이지에 10개씩, 최신 글(id 큰 순)부터.
    // One page of the question list (book 3-02).
    // page: a 0-based page number. 10 per page, newest first (largest id first).
    public Page<Question> getList(int page) {
        // PageRequest.of(페이지번호, 한 페이지 크기, 정렬): "몇 번째 페이지를 몇 개씩, 어떤 순서로" 가져올지 정한다.
        // 교재는 createDate 내림차순이지만, 300개를 한꺼번에 만들면 시간이 거의 같아 순서가 섞일 수 있어 id 내림차순을 쓴다.
        // PageRequest.of(page, size, sort): decides "which page, how many per page, in what order".
        // The book sorts by createDate descending; when 300 rows are created at once their times are nearly equal and can tie, so we sort by id descending.
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("id")));
        // findAll(pageable) → SELECT ... ORDER BY id DESC LIMIT 10 OFFSET (page*10) + 전체 개수를 세는 COUNT 쿼리
        // findAll(pageable) → SELECT ... ORDER BY id DESC LIMIT 10 OFFSET (page*10) plus a COUNT query for the total
        return questionRepository.findAll(pageable);
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
    // author: 글쓴이 (로그인한 회원, 028-01)
    // author: the writer (the logged-in member, 028-01)
    public Question create(String subject, String content, SiteUser author) {
        Question question = new Question();
        question.setSubject(subject);
        question.setContent(content);
        question.setAuthor(author);
        return questionRepository.save(question);
    }

    // 질문 수정 (교재 3-09). 수정일시는 JPA Auditing(@LastModifiedDate)이 자동으로 갱신한다.
    // Edits a question (book 3-09). JPA Auditing (@LastModifiedDate) updates the modified date automatically.
    @Transactional
    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        // save → UPDATE question SET ... WHERE id = ?
        questionRepository.save(question);
    }

    // 질문 삭제 (교재 3-09). cascade = REMOVE 라서 답변도 함께 삭제된다 (005-01).
    // Deletes a question (book 3-09). With cascade = REMOVE its answers are deleted too (005-01).
    @Transactional
    public void delete(Question question) {
        questionRepository.delete(question);
    }

    // 추천 (교재 3-10). Set 이라서 이미 추천한 회원이면 그대로다.
    // Recommend (book 3-10). It's a Set, so a member who already recommended is simply ignored.
    @Transactional
    public void vote(Question question, SiteUser siteUser) {
        question.getVoter().add(siteUser);
        // → INSERT INTO question_voter (question_id, voter_id) VALUES (?, ?)
        questionRepository.save(question);
    }
}
