package com.back.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// 회원가입 폼 입력값 (record, 설명은 QuestionForm.java 참고).
// The sign-up form's input (a record; see QuestionForm.java for details).
public record UserCreateForm(
        @Size(min = 3, max = 25, message = "사용자ID는 3자 이상 25자 이하로 입력해주세요.")
        @NotBlank(message = "사용자ID는 필수항목입니다.")
        String username,

        @NotBlank(message = "비밀번호는 필수항목입니다.")
        String password1,

        // 비밀번호 확인. 두 값이 같은지는 어노테이션으로 검사할 수 없어 컨트롤러에서 비교한다.
        // Password confirmation. Whether the two match can't be checked with an annotation, so the controller compares them.
        @NotBlank(message = "비밀번호 확인은 필수항목입니다.")
        String password2,

        @NotBlank(message = "이메일은 필수항목입니다.")
        // @Email: 이메일 형식(abc@def.com)인지 검사
        // @Email: checks the value looks like an email (abc@def.com)
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String email
) {
}
