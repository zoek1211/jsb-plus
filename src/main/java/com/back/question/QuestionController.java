package com.back.question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

// 질문 관련 웹 요청을 처리하는 컨트롤러. MainController 와 같은 @Controller 다.
// The controller that handles question-related web requests. A @Controller just like MainController.
@Controller
// final 필드(questionRepository)를 생성자로 주입받는다 (TestInitData 에서 설명).
// Receives the final field (questionRepository) through the constructor (explained in TestInitData).
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionRepository questionRepository;

    // GET /question/list → 질문 목록 화면.
    // 아직 템플릿 엔진(타임리프)이 없어서, 14163 27강처럼 자바 문자열로 HTML 을 직접 만들어 돌려준다.
    // HTML 이 길어질수록 읽고 고치기 어렵다는 것을 느끼는 단계 → 011-02 에서 타임리프로 바꾼다.
    // GET /question/list → the question list page.
    // There's no template engine (Thymeleaf) yet, so like 14163 lecture 27 we build the HTML as a Java string.
    // This step shows how hard HTML-in-Java gets to read and edit → replaced with Thymeleaf in 011-02.
    @GetMapping("/question/list")
    @ResponseBody
    public String list() {
        List<Question> questions = questionRepository.findAll();

        // stream(): 목록을 하나씩 흘려보내며 가공한다.
        // map(...): 질문 하나를 "<li>번호 / 제목</li>" 문자열로 바꾼다.
        // Collectors.joining(...): 바뀐 문자열들을 줄바꿈으로 이어 붙인다.
        // stream(): processes the list one item at a time.
        // map(...): turns each question into the string "<li>id / subject</li>".
        // Collectors.joining(...): joins those strings with line breaks.
        String questionLiHtml = questions
                .stream()
                .map(q -> "<li>%d / %s</li>".formatted(q.getId(), q.getSubject()))
                .collect(Collectors.joining("\n"));

        // """ ... """: 텍스트 블록. 여러 줄 문자열을 그대로 쓸 수 있다. %s 자리에 위의 <li> 목록이 들어간다.
        // """ ... """: a text block, for writing multi-line strings as-is. The <li> list above goes into %s.
        return """
                <!DOCTYPE html>
                <html lang="ko">
                <head>
                    <meta charset="UTF-8">
                    <title>질문 목록</title>
                </head>
                <body>
                    <h1>질문 목록</h1>
                    <ul>
                        %s
                    </ul>
                </body>
                </html>
                """.formatted(questionLiHtml);
    }
}
