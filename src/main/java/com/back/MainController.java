package com.back;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// 이 클래스가 웹 요청을 처리하는 컨트롤러임을 스프링에 알린다.
// 앱이 시작될 때 스프링이 이 클래스를 찾아 객체를 하나 만들어 둔다. 빼면 아래 URL은 404가 된다.
// Tells Spring that this class handles web requests.
// Spring finds it at startup and creates one object from it. Remove it and the URL below returns 404.
@Controller
public class MainController {

    // GET / (사이트 첫 화면) → 질문 목록으로 보낸다 (교재 2-08, 14163 33강).
    // 연습용이던 "SBB", "/hello" 응답(002-01~02)은 실제 서비스에는 필요 없어 지웠다.
    // GET / (the site's front page) → sends the user to the question list (book 2-08, 14163 lecture 33).
    // The practice responses "SBB" and "/hello" (002-01–02) aren't needed for the real service, so they were removed.
    @GetMapping("/")
    public String index() {
        // "redirect:주소": 템플릿 이름이 아니라 "이 주소로 다시 요청하라"는 응답(HTTP 302)을 브라우저에 보낸다.
        // 브라우저는 자동으로 /question/list 를 다시 요청하고, 주소창도 /question/list 로 바뀐다.
        // "redirect:<url>": not a template name; it sends the browser a "request this URL instead" response (HTTP 302).
        // The browser automatically requests /question/list, and the address bar changes to /question/list.
        return "redirect:/question/list";
    }
}
