package com.back;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

// 이 클래스가 웹 요청을 처리하는 컨트롤러임을 스프링에 알린다.
// 앱이 시작될 때 스프링이 이 클래스를 찾아 객체를 하나 만들어 둔다. 빼면 아래 URL들은 404가 된다.
// Tells Spring that this class handles web requests.
// Spring finds it at startup and creates one object from it. Remove it and the URLs below return 404.
@Controller
public class MainController {

    // GET 방식으로 "/" 에 요청이 오면 이 메서드를 호출한다. 이렇게 URL과 연결된 메서드를 "액션 메서드"라고 한다.
    // Calls this method for a GET request to "/". A method linked to a URL like this is called an "action method".
    @GetMapping("/")
    // @ResponseBody: 메서드가 반환한 값을 "템플릿 이름"이 아니라 "응답 본문(브라우저에 보낼 내용)" 그 자체로 쓰라는 뜻.
    // 문자열을 반환하면 그 글자가 그대로 브라우저에 표시된다. 객체를 반환하면 JSON으로 바뀌어 전달된다.
    // 이것을 빼면 스프링은 "SBB"라는 이름의 템플릿을 찾으려 하고, 템플릿이 없으니 에러가 난다 (002-01에서 본 것과 같은 원리).
    // @ResponseBody: use the return value as the response body itself (what the browser receives), not as a template name.
    // A returned String shows up in the browser as-is. A returned object is converted to JSON.
    // Without it, Spring looks for a template named "SBB", can't find one, and errors (same idea as in 002-01).
    @ResponseBody
    // void 대신 String을 반환한다. 이 문자열이 곧 응답 내용이 된다.
    // Returns a String instead of void. This string becomes the response.
    public String index() {
        return "SBB";
    }

    // GET 방식으로 "/hello" 에 요청이 오면 이 메서드를 호출한다.
    // Calls this method for a GET request to "/hello".
    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        return "안녕하세요 ^ ^";
    }
}
