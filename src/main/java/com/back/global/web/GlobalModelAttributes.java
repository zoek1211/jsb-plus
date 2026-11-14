package com.back.global.web;

import com.back.global.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

// 모든 컨트롤러의 화면(model)에 공통으로 넣어 줄 값 (032-01).
// @ControllerAdvice: 모든 @Controller 에 함께 적용되는 설정 클래스.
// Values added to the model of every controller's page (032-01).
// @ControllerAdvice: a class whose settings apply to every @Controller.
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributes {
    private final CommonUtil commonUtil;

    // @ModelAttribute("commonUtil"): 이 메서드의 반환값을 모든 화면의 model 에 "commonUtil" 이라는 이름으로 넣는다 (14168 19강과 같은 원리).
    // 교재는 템플릿에서 ${@commonUtil.markdown(...)} 처럼 스프링 빈을 직접 부르지만,
    // 우리가 쓰는 타임리프 3.1.5 는 보안상 th:utext 등에서 @빈 접근을 막는다
    // ("Instantiation of new objects and access to static classes or parameters is forbidden" 오류, 032-01 에서 확인).
    // model 에 들어간 일반 객체의 메서드 호출은 허용되므로 ${commonUtil.markdown(...)} 으로 쓴다.
    // @ModelAttribute("commonUtil"): puts this method's return value into every page's model under the name "commonUtil" (same idea as 14168 lecture 19).
    // The book calls the Spring bean straight from the template as ${@commonUtil.markdown(...)},
    // but the Thymeleaf 3.1.5 we use blocks @bean access in th:utext and similar places for security
    // (the "Instantiation of new objects and access to static classes or parameters is forbidden" error, seen in 032-01).
    // Calling a method on a plain object in the model is allowed, so we write ${commonUtil.markdown(...)}.
    @ModelAttribute("commonUtil")
    public CommonUtil commonUtil() {
        return commonUtil;
    }
}
