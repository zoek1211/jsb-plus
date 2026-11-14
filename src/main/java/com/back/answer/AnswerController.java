package com.back.answer;

import com.back.question.Question;
import com.back.question.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

// 답변 관련 웹 요청을 처리하는 컨트롤러. 모든 URL 이 /answer 로 시작한다.
// The controller for answer-related web requests. Every URL starts with /answer.
@Controller
@RequiredArgsConstructor
@RequestMapping("/answer")
public class AnswerController {
    private final QuestionService questionService;
    private final AnswerService answerService;

    // POST /answer/create/{id}: 질문 상세 화면의 답변 폼을 제출하면 실행된다.
    // @PostMapping: GET(조회)이 아니라 POST(생성) 요청만 받는다. 데이터를 "만드는" 요청은 POST 를 쓴다 (14168 7강).
    // POST /answer/create/{id}: runs when the answer form on the question detail page is submitted.
    // @PostMapping: accepts only POST (create) requests, not GET (read). Requests that "create" data use POST (14168 lecture 7).
    @PostMapping("/create/{id}")
    public String createAnswer(
            @PathVariable int id,
            // @RequestParam("content"): 폼의 <textarea name="content"> 에 입력한 값을 받는다.
            // @RequestParam("content"): receives the value typed into the form's <textarea name="content">.
            @RequestParam("content") String content
    ) {
        Question question = questionService.getQuestion(id);
        answerService.create(question, content);
        // 저장 후 상세 화면으로 다시 이동 (POST 뒤 redirect: 새로고침해도 답변이 두 번 등록되지 않는다).
        // After saving, go back to the detail page (redirect after POST: refreshing won't submit the answer twice).
        return "redirect:/question/detail/%d".formatted(id);
    }
}
