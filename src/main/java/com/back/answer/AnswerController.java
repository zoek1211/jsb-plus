package com.back.answer;

import com.back.question.Question;
import com.back.question.QuestionService;
import com.back.user.SiteUser;
import com.back.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Objects;

// 답변 관련 웹 요청을 처리하는 컨트롤러. 모든 URL 이 /answer 로 시작한다.
// The controller for answer-related web requests. Every URL starts with /answer.
@Controller
@RequiredArgsConstructor
@RequestMapping("/answer")
public class AnswerController {
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;

    // POST /answer/create/{id}: 질문 상세 화면의 답변 폼을 제출하면 실행된다.
    // @PostMapping: GET(조회)이 아니라 POST(생성) 요청만 받는다. 데이터를 "만드는" 요청은 POST 를 쓴다 (14168 7강).
    // POST /answer/create/{id}: runs when the answer form on the question detail page is submitted.
    // @PostMapping: accepts only POST (create) requests, not GET (read). Requests that "create" data use POST (14168 lecture 7).
    @PostMapping("/create/{id}")
    // 로그인한 사용자만 답변 등록 (교재 3-08, 설명은 QuestionController.showCreate)
    // Only logged-in users can post answers (book 3-08; see QuestionController.showCreate)
    @PreAuthorize("isAuthenticated()")
    public String createAnswer(
            Model model,
            // 현재 로그인한 사용자 / The currently logged-in user
            Principal principal,
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

        SiteUser author = userService.getUser(principal.getName());
        answerService.create(question, answerForm.content(), author);
        // 저장 후 상세 화면으로 다시 이동 (POST 뒤 redirect: 새로고침해도 답변이 두 번 등록되지 않는다).
        // After saving, go back to the detail page (redirect after POST: refreshing won't submit the answer twice).
        return "redirect:/question/detail/%d".formatted(id);
    }

    // 글쓴이 본인인지 확인 (029-02). 아니면 403. 설명은 QuestionController.checkAuthor 참고.
    // Checks the user is the author (029-02). Otherwise 403. See QuestionController.checkAuthor.
    private void checkAuthor(Answer answer, Principal principal, String message) {
        String authorName = answer.getAuthor() == null ? null : answer.getAuthor().getUsername();
        if (!Objects.equals(authorName, principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, message);
        }
    }

    // GET /answer/modify/{id} → 답변 수정 폼 (교재 3-09)
    // GET /answer/modify/{id} → the answer edit form (book 3-09)
    @GetMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String showModify(Model model, @PathVariable int id, Principal principal) {
        Answer answer = answerService.getAnswer(id);
        checkAuthor(answer, principal, "수정권한이 없습니다.");
        model.addAttribute("answerForm", new AnswerForm(answer.getContent()));
        model.addAttribute("answerId", id);
        return "answer_form";
    }

    // PUT /answer/modify/{id} → 답변 수정 처리 (14168 41강: 수정은 PUT)
    // PUT /answer/modify/{id} → handles the answer edit (14168 lecture 41: edits use PUT)
    @PutMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String modify(
            Model model,
            @PathVariable int id,
            Principal principal,
            @ModelAttribute("answerForm") @Valid AnswerForm answerForm,
            BindingResult bindingResult
    ) {
        Answer answer = answerService.getAnswer(id);
        checkAuthor(answer, principal, "수정권한이 없습니다.");
        if (bindingResult.hasErrors()) {
            model.addAttribute("answerId", id);
            return "answer_form";
        }
        answerService.modify(answer, answerForm.content());
        return "redirect:/question/detail/%d".formatted(answer.getQuestion().getId());
    }

    // DELETE /answer/delete/{id} → 답변 삭제 처리 (14168 41~43강: 삭제는 DELETE)
    // DELETE /answer/delete/{id} → handles answer deletion (14168 lectures 41–43: deletes use DELETE)
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(@PathVariable int id, Principal principal) {
        Answer answer = answerService.getAnswer(id);
        checkAuthor(answer, principal, "삭제권한이 없습니다.");
        // 삭제 전에 질문 id 를 기억해 둔다 (삭제 후 상세 화면으로 돌아가기 위해)
        // Remember the question id before deleting (to go back to its detail page afterwards)
        int questionId = answer.getQuestion().getId();
        answerService.delete(answer);
        return "redirect:/question/detail/%d".formatted(questionId);
    }
}
