package com.back.answer;

import com.back.question.Question;
import com.back.question.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
            Model model,
            @PathVariable int id,
            // 폼 값을 AnswerForm 으로 받고 @Valid 로 검사한다 (019-01). 이름 "answerForm" 은 상세 화면의 th:object 와 맞춘다.
            // Receive the form value as AnswerForm and check it with @Valid (019-01). The name "answerForm" matches th:object on the detail page.
            @ModelAttribute("answerForm") @Valid AnswerForm answerForm,
            BindingResult bindingResult
    ) {
        Question question = questionService.getQuestion(id);

        // 내용이 비어 있으면 저장하지 않고 상세 화면을 오류와 함께 다시 보여준다. 상세 화면에 필요한 question 도 다시 담는다.
        // If the content is empty, don't save; show the detail page again with the error. Put back the question the page needs.
        if (bindingResult.hasErrors()) {
            model.addAttribute("question", question);
            return "question_detail";
        }

        answerService.create(question, answerForm.content());
        // 저장 후 상세 화면으로 다시 이동 (POST 뒤 redirect: 새로고침해도 답변이 두 번 등록되지 않는다).
        // After saving, go back to the detail page (redirect after POST: refreshing won't submit the answer twice).
        return "redirect:/question/detail/%d".formatted(id);
    }
}
