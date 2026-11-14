package com.back.question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

// 질문 관련 웹 요청을 처리하는 컨트롤러. MainController 와 같은 @Controller 다.
// The controller that handles question-related web requests. A @Controller just like MainController.
@Controller
// final 필드(questionService)를 생성자로 주입받는다 (TestInitData 에서 설명).
// Receives the final field (questionService) through the constructor (explained in TestInitData).
@RequiredArgsConstructor
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
    @GetMapping("/question/list")
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
    @GetMapping("/question/detail/{id}")
    public String detail(Model model, @PathVariable int id) {
        Question question = questionService.getQuestion(id);
        model.addAttribute("question", question);
        return "question_detail";
    }
}
