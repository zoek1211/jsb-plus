package com.back.question;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// 질문 등록 폼의 입력값을 담는 객체 (폼 DTO).
// record: 필드·생성자·접근자(subject(), content())를 자동으로 만들어 주는 간단한 데이터 클래스 (14168 40강).
// 교재는 @Getter @Setter 가 붙은 일반 클래스를 쓰지만, 폼 값은 한 번 받으면 바꿀 일이 없어 record 가 더 간결하다.
// The object holding the question form's input (a form DTO).
// record: a compact data class that generates the fields, constructor and accessors (subject(), content()) for you (14168 lecture 40).
// The book uses a regular class with @Getter @Setter; form values never change once received, so a record is simpler.
public record QuestionForm(
        // @NotBlank: null, "", "   " (공백만) 이면 오류. message 는 화면에 보여줄 문구.
        // @NotBlank: an error if null, "" or only spaces. message is the text shown on the page.
        @NotBlank(message = "제목은 필수항목입니다.")
        // @Size(max = 200): 200자를 넘으면 오류 (Question.subject 컬럼 길이와 맞춤).
        // @Size(max = 200): an error if longer than 200 characters (matches the Question.subject column length).
        @Size(max = 200, message = "제목은 200자 이하로 입력해주세요.")
        String subject,

        @NotBlank(message = "내용은 필수항목입니다.")
        String content
) {
}
