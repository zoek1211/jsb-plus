package com.back;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public void index() {
        System.out.println("index() 메서드 호출됨!");
    }

    @GetMapping("/hello")
    public void hello() {
        System.out.println("hello() 메서드 호출됨!");
    }
}
