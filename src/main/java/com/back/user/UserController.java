package com.back.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// 회원 관련 웹 요청 (/user/...) (교재 3-06).
// Member-related web requests (/user/...) (book 3-06).
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    // GET /user/signup → 회원가입 폼 (빈 폼 객체를 "form" 으로)
    // GET /user/signup → the sign-up form (an empty form object as "form")
    @GetMapping("/signup")
    public String showSignup(@ModelAttribute("form") UserCreateForm form) {
        return "signup_form";
    }

    // POST /user/signup → 가입 처리
    // POST /user/signup → handles sign-up
    @PostMapping("/signup")
    public String signup(@ModelAttribute("form") @Valid UserCreateForm form, BindingResult bindingResult) {
        // 1) 어노테이션 검사(@NotBlank 등) 실패
        // 1) Annotation checks (@NotBlank, ...) failed
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }

        // 2) 비밀번호 확인 불일치. rejectValue(필드, 오류코드, 메시지): 특정 필드에 오류를 직접 추가한다.
        // 2) Password confirmation mismatch. rejectValue(field, errorCode, message): adds an error to a specific field by hand.
        if (!form.password1().equals(form.password2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
            return "signup_form";
        }

        // 3) 이미 있는 아이디·이메일 → DB 유일 제약 위반. reject(오류코드, 메시지): 특정 필드가 아닌 폼 전체 오류.
        // 3) Existing username/email → violates the database's unique constraint. reject(errorCode, message): a form-wide error, not tied to a field.
        try {
            userService.create(form.username(), form.email(), form.password1());
        } catch (DataIntegrityViolationException e) {
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signup_form";
        }

        return "redirect:/";
    }
}
