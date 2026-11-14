package com.back.answer;

import jakarta.validation.constraints.NotBlank;

// 답변 등록 폼의 입력값 (QuestionForm 과 같은 record 방식, 설명은 QuestionForm.java 참고).
// The answer form's input (a record like QuestionForm; see QuestionForm.java for details).
public record AnswerForm(
        @NotBlank(message = "내용은 필수항목입니다.")
        String content
) {
}
