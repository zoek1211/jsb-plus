package com.back.question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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

    // GET /question/list → 질문 목록 화면.
    // @ResponseBody 가 없으므로 반환값 "question_list" 는 "템플릿 이름"이다 (002-01 에서 배운 것).
    // 타임리프가 templates/question_list.html 을 찾아 HTML 을 만든다. 이제 자바 코드에 HTML 이 없다.
    // GET /question/list → the question list page.
    // No @ResponseBody, so the return value "question_list" is a "template name" (as learned in 002-01).
    // Thymeleaf finds templates/question_list.html and builds the HTML. No more HTML inside Java code.
    @GetMapping("/list")
    // Model: 컨트롤러가 템플릿에 넘길 데이터를 담는 상자. 스프링이 매개변수로 넣어준다.
    // Model: a box holding the data the controller passes to the template. Spring passes it in as a parameter.
    public String list(Model model) {
        List<Question> questionList = questionService.getList();
        // "questionList" 라는 이름으로 담으면 템플릿에서 ${questionList} 로 꺼내 쓸 수 있다.
        // Stored under the name "questionList", the template can read it as ${questionList}.
        model.addAttribute("questionList", questionList);
        return "question_list";
    }

    // GET /question/detail/1, /question/detail/2 ... → 질문 상세 화면.
    // {id}: URL 경로의 일부를 변수로 받는 자리. @PathVariable 이 그 값을 int id 매개변수에 넣어준다.
    // 빼면(@PathVariable 없이) 스프링은 id 가 URL 의 어디에서 오는지 몰라 오류가 난다.
    // GET /question/detail/1, /question/detail/2 ... → the question detail page.
    // {id}: a placeholder that captures part of the URL path. @PathVariable puts that value into the int id parameter.
    // Without @PathVariable, Spring doesn't know where id comes from and fails.
    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable int id) {
        Question question = questionService.getQuestion(id);
        model.addAttribute("question", question);
        return "question_detail";
    }

    // GET /question/create → 질문 등록 폼 화면.
    // GET /question/create → the question form page.
    @GetMapping("/create")
    public String showCreate() {
        return "question_form";
    }

    // POST /question/create → 폼 제출 처리 (저장 후 목록으로).
    // 같은 URL 이라도 GET(폼 보여주기)과 POST(저장하기)는 스프링이 구분한다 (14168 26강). 교재처럼 doCreate 같은 별도 이름이 필요 없다.
    // POST /question/create → handles the submitted form (save, then go to the list).
    // Spring tells GET (show form) and POST (save) apart even on the same URL (14168 lecture 26), so no separate name like doCreate is needed.
    @PostMapping("/create")
    public String create(
            // 폼의 <input name="subject">, <textarea name="content"> 값
            // The values from the form's <input name="subject"> and <textarea name="content">
            @RequestParam("subject") String subject,
            @RequestParam("content") String content
    ) {
        questionService.create(subject, content);
        // POST 처리 후에는 redirect → 새로고침해도 다시 등록되지 않는다 (14168 28강).
        // Redirect after POST → refreshing won't submit again (14168 lecture 28).
        return "redirect:/question/list";
    }
}
