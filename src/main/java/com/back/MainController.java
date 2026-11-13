package com.back;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// 이 클래스가 웹 요청을 처리하는 컨트롤러임을 스프링에 알린다.
// 앱이 시작될 때 스프링이 이 클래스를 찾아 객체를 하나 만들어 둔다. 빼면 아래 URL들은 404가 된다.
// Tells Spring that this class handles web requests.
// Spring finds it at startup and creates one object from it. Remove it and the URLs below return 404.
@Controller
public class MainController {

    // GET 방식으로 "/" 에 요청이 오면 이 메서드를 호출한다. 이렇게 URL과 연결된 메서드를 "액션 메서드"라고 한다.
    // Calls this method for a GET request to "/". A method linked to a URL like this is called an "action method".
    @GetMapping("/")
    // void = 아무것도 반환하지 않는다. 그래서 스프링은 브라우저에 보낼 내용을 몰라 500 에러가 난다 (002-02에서 해결).
    // void = returns nothing, so Spring doesn't know what to send the browser and responds with 500 (fixed in 002-02).
    public void index() {
        // 서버 콘솔(로그)에 출력된다. 브라우저에는 보이지 않는다.
        // Prints to the server console (log). The browser never sees it.
        System.out.println("index() 메서드 호출됨!");
    }

    // GET 방식으로 "/hello" 에 요청이 오면 이 메서드를 호출한다.
    // Calls this method for a GET request to "/hello".
    @GetMapping("/hello")
    public void hello() {
        System.out.println("hello() 메서드 호출됨!");
    }
}
