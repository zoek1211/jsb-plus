package com.back.question;

import com.back.answer.AnswerForm;
import com.back.user.SiteUser;
import com.back.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Objects;

// 질문 관련 웹 요청을 처리하는 컨트롤러. MainController 와 같은 @Controller 다.
// The controller that handles question-related web requests. A @Controller just like MainController.
@Controller
// final 필드(questionService)를 생성자로 주입받는다 (TestInitData 에서 설명).
// Receives the final field (questionService) through the constructor (explained in TestInitData).
@RequiredArgsConstructor
// @RequestMapping("/question"): 이 클래스의 모든 URL 앞에 /question 을 붙인다 (교재 2-11, 14163 36강).
// 그래서 아래 @GetMapping("/list") 는 실제로 /question/list, @GetMapping("/detail/{id}") 는 /question/detail/{id} 가 된다.
// 공통 접두어를 한 곳에서 관리해 중복과 오타를 줄인다.
// @RequestMapping("/question"): prefixes every URL in this class with /question (book 2-11, 14163 lecture 36).
// So @GetMapping("/list") below is really /question/list, and @GetMapping("/detail/{id}") is /question/detail/{id}.
// Keeping the shared prefix in one place reduces repetition and typos.
@RequestMapping("/question")
public class QuestionController {
    // 리포지터리 대신 서비스를 쓴다 (012-01). 컨트롤러는 DB를 직접 다루지 않는다.
    // Uses the service instead of the repository (012-01). The controller doesn't touch the database directly.
    private final QuestionService questionService;
    // 로그인한 회원(글쓴이)을 찾기 위해 (028-01)
    // To look up the logged-in member (the author) (028-01)
    private final UserService userService;

    // GET /question/list → 질문 목록 화면.
    // @ResponseBody 가 없으므로 반환값 "question_list" 는 "템플릿 이름"이다 (002-01 에서 배운 것).
    // 타임리프가 templates/question_list.html 을 찾아 HTML 을 만든다. 이제 자바 코드에 HTML 이 없다.
    // GET /question/list → the question list page.
    // No @ResponseBody, so the return value "question_list" is a "template name" (as learned in 002-01).
    // Thymeleaf finds templates/question_list.html and builds the HTML. No more HTML inside Java code.
    @GetMapping("/list")
    // Model: 컨트롤러가 템플릿에 넘길 데이터를 담는 상자. 스프링이 매개변수로 넣어준다.
    // Model: a box holding the data the controller passes to the template. Spring passes it in as a parameter.
    public String list(
            Model model,
            // ?page=3 처럼 주소 뒤에 붙는 값을 받는다. 없으면 기본값 0 (첫 페이지) (교재 3-02).
            // Receives a value appended to the URL like ?page=3. Defaults to 0 (the first page) if absent (book 3-02).
            @RequestParam(value = "page", defaultValue = "0") int page,
            // ?kw=검색어 (교재 3-13). 없으면 빈 문자열 → 전체 목록.
            // ?kw=keyword (book 3-13). Empty string if absent → the full list.
            @RequestParam(value = "kw", defaultValue = "") String kw
    ) {
        // Page: 이번 페이지의 질문들 + 전체 개수, 전체 페이지 수, 이전/다음 페이지 여부 같은 정보를 함께 담은 객체.
        // Page: holds this page's questions plus info like the total count, total pages, and whether there's a previous/next page.
        Page<Question> paging = questionService.getList(page, kw);
        // 템플릿에서 ${paging} 으로 꺼내 쓴다.
        // The template reads it as ${paging}.
        model.addAttribute("paging", paging);
        // 검색창에 검색어를 다시 보여주고, 페이지 링크에도 붙이기 위해
        // To show the keyword again in the search box and to attach it to the page links
        model.addAttribute("kw", kw);
        return "question_list";
    }

    // GET /question/detail/1, /question/detail/2 ... → 질문 상세 화면.
    // {id}: URL 경로의 일부를 변수로 받는 자리. @PathVariable 이 그 값을 int id 매개변수에 넣어준다.
    // 빼면(@PathVariable 없이) 스프링은 id 가 URL 의 어디에서 오는지 몰라 오류가 난다.
    // GET /question/detail/1, /question/detail/2 ... → the question detail page.
    // {id}: a placeholder that captures part of the URL path. @PathVariable puts that value into the int id parameter.
    // Without @PathVariable, Spring doesn't know where id comes from and fails.
    @GetMapping("/detail/{id}")
    // @ModelAttribute("answerForm"): 상세 화면 아래 답변 폼(th:object="${answerForm}")에 쓸 빈 폼 객체 (019-01).
    // @ModelAttribute("answerForm"): an empty form object for the answer form at the bottom of the page (th:object="${answerForm}") (019-01).
    public String detail(Model model, @PathVariable int id, @ModelAttribute("answerForm") AnswerForm answerForm) {
        Question question = questionService.getQuestion(id);
        model.addAttribute("question", question);
        return "question_detail";
    }

    // GET /question/create → 질문 등록 폼 화면.
    // GET /question/create → the question form page.
    @GetMapping("/create")
    // @PreAuthorize("isAuthenticated()"): 로그인한 사용자만 실행할 수 있다 (교재 3-08).
    // 로그인하지 않았으면 시큐리티가 로그인 화면(/user/login)으로 보낸다. SecurityConfig 의 @EnableMethodSecurity 가 있어야 동작한다.
    // @PreAuthorize("isAuthenticated()"): only logged-in users may run this (book 3-08).
    // If not logged in, Security sends the user to the login page (/user/login). Requires @EnableMethodSecurity in SecurityConfig.
    @PreAuthorize("isAuthenticated()")
    // @ModelAttribute("form"): 빈 QuestionForm 을 만들어 "form" 이라는 이름으로 model 에 담는다.
    // 템플릿의 th:object="${form}" 이 이 객체를 쓴다. 처음 화면에서는 값이 모두 비어 있다 (14168 18강).
    // @ModelAttribute("form"): creates an empty QuestionForm and puts it in the model under the name "form".
    // The template's th:object="${form}" uses it. On first display all its values are empty (14168 lecture 18).
    public String showCreate(@ModelAttribute("form") QuestionForm form) {
        return "question_form";
    }

    // POST /question/create → 폼 제출 처리 (저장 후 목록으로).
    // 같은 URL 이라도 GET(폼 보여주기)과 POST(저장하기)는 스프링이 구분한다 (14168 26강). 교재처럼 doCreate 같은 별도 이름이 필요 없다.
    // POST /question/create → handles the submitted form (save, then go to the list).
    // Spring tells GET (show form) and POST (save) apart even on the same URL (14168 lecture 26), so no separate name like doCreate is needed.
    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String create(
            // Principal: 현재 로그인한 사용자 정보. getName() 은 로그인 아이디 (교재 3-08).
            // Principal: the currently logged-in user. getName() is the login username (book 3-08).
            Principal principal,
            // @ModelAttribute: 폼의 subject, content 값을 QuestionForm 하나로 모아 받는다 (@RequestParam 여러 개 대신, 14168 12강).
            // @Valid: QuestionForm 에 붙은 @NotBlank, @Size 규칙으로 검사한다.
            // @ModelAttribute: collects the form's subject and content into one QuestionForm (instead of several @RequestParams, 14168 lecture 12).
            // @Valid: checks it against the @NotBlank and @Size rules on QuestionForm.
            @ModelAttribute("form") @Valid QuestionForm form,
            // BindingResult: 검사 결과(오류 목록). 반드시 @Valid 매개변수 "바로 뒤"에 와야 한다.
            // 이게 없으면 검사 실패 시 스프링이 바로 400 오류를 내 버린다. 있으면 우리가 직접 처리할 수 있다 (14168 13강).
            // BindingResult: the check results (list of errors). It must come "right after" the @Valid parameter.
            // Without it, Spring answers with a 400 error as soon as a check fails. With it, we can handle failures ourselves (14168 lecture 13).
            BindingResult bindingResult
    ) {
        // 오류가 있으면 저장하지 않고 폼을 다시 보여준다. 입력했던 값과 오류 메시지가 함께 표시된다 (14168 9~10강).
        // On errors, don't save; show the form again. The values the user typed and the error messages are shown (14168 lectures 9–10).
        if (bindingResult.hasErrors()) {
            return "question_form";
        }

        // 로그인 아이디로 회원을 찾아 글쓴이로 저장
        // Look up the member by login username and store them as the author
        SiteUser author = userService.getUser(principal.getName());
        questionService.create(form.subject(), form.content(), author);
        // POST 처리 후에는 redirect → 새로고침해도 다시 등록되지 않는다 (14168 28강).
        // Redirect after POST → refreshing won't submit again (14168 lecture 28).
        return "redirect:/question/list";
    }

    // 글쓴이 본인인지 확인 (029-01). 아니면 403 Forbidden.
    // Objects.equals: author 가 null(글쓴이 없는 예전 글)이어도 안전하게 비교한다.
    // Checks the user is the author (029-01). Otherwise 403 Forbidden.
    // Objects.equals: compares safely even when author is null (old posts without an author).
    private void checkAuthor(Question question, Principal principal, String message) {
        String authorName = question.getAuthor() == null ? null : question.getAuthor().getUsername();
        if (!Objects.equals(authorName, principal.getName())) {
            // ResponseStatusException: 원하는 HTTP 상태 코드로 응답하게 하는 예외
            // ResponseStatusException: an exception that makes the response use the HTTP status we choose
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, message);
        }
    }

    // GET /question/modify/{id} → 수정 폼 (교재 3-09). 등록 폼(question_form.html)을 함께 쓴다.
    // GET /question/modify/{id} → the edit form (book 3-09). Shares the create form (question_form.html).
    @GetMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String showModify(Model model, @PathVariable int id, Principal principal) {
        Question question = questionService.getQuestion(id);
        checkAuthor(question, principal, "수정권한이 없습니다.");
        // 기존 제목·내용을 채운 폼. record 라서 생성자로 바로 만든다.
        // A form pre-filled with the current subject/content. It's a record, so we build it with the constructor.
        model.addAttribute("form", new QuestionForm(question.getSubject(), question.getContent()));
        // 템플릿이 "등록"인지 "수정"인지 구분하도록 질문 id 를 넘긴다.
        // Pass the question id so the template can tell "create" from "edit".
        model.addAttribute("questionId", id);
        return "question_form";
    }

    // PUT /question/modify/{id} → 수정 처리. "수정(갱신)"은 HTTP 메서드 PUT 으로 표현한다 (14168 41강).
    // 폼은 POST 로 보내지만 숨은 _method=put 덕분에 이 메서드로 온다 (application.yaml 의 hiddenmethod 필터).
    // PUT /question/modify/{id} → handles the edit. "Updating" is expressed with the HTTP method PUT (14168 lecture 41).
    // The form is sent as POST, but the hidden _method=put routes it here (the hiddenmethod filter in application.yaml).
    @PutMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String modify(
            Model model,
            @PathVariable int id,
            Principal principal,
            @ModelAttribute("form") @Valid QuestionForm form,
            BindingResult bindingResult
    ) {
        Question question = questionService.getQuestion(id);
        checkAuthor(question, principal, "수정권한이 없습니다.");
        if (bindingResult.hasErrors()) {
            model.addAttribute("questionId", id);
            return "question_form";
        }
        questionService.modify(question, form.subject(), form.content());
        return "redirect:/question/detail/%d".formatted(id);
    }

    // DELETE /question/delete/{id} → 삭제 처리 (교재 3-09).
    // 교재는 GET 링크(<a href>)로 삭제하지만, 주소만 열어도 삭제되는 GET 은 위험하므로 DELETE + CSRF 토큰으로 받는다 (14168 41~43강).
    // DELETE /question/delete/{id} → handles deletion (book 3-09).
    // The book deletes via a GET link (<a href>); since a GET that deletes just by opening a URL is dangerous, we accept DELETE with a CSRF token (14168 lectures 41–43).
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(@PathVariable int id, Principal principal) {
        Question question = questionService.getQuestion(id);
        checkAuthor(question, principal, "삭제권한이 없습니다.");
        questionService.delete(question);
        return "redirect:/";
    }

    // POST /question/vote/{id} → 질문 추천 (교재 3-10).
    // 교재는 GET 링크로 추천하지만, 데이터를 바꾸는 요청이므로 POST + CSRF 토큰으로 받는다 (029-01 과 같은 이유).
    // POST /question/vote/{id} → recommends a question (book 3-10).
    // The book recommends via a GET link; since it changes data, we accept POST with a CSRF token (same reason as 029-01).
    @PostMapping("/vote/{id}")
    @PreAuthorize("isAuthenticated()")
    public String vote(@PathVariable int id, Principal principal) {
        Question question = questionService.getQuestion(id);
        SiteUser siteUser = userService.getUser(principal.getName());
        questionService.vote(question, siteUser);
        return "redirect:/question/detail/%d".formatted(id);
    }
}
