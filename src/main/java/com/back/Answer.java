package com.back;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

// 답변 엔티티 → answer 테이블. @Id, @GeneratedValue, @Column 의 뜻은 Question.java 와 같다.
// Answer entity → answer table. @Id, @GeneratedValue and @Column mean the same as in Question.java.
@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;

    private LocalDateTime createDate;

    // @ManyToOne: "여러(Many) 답변이 하나(One)의 질문에 속한다"는 관계.
    // DB에는 Question 객체 전체가 아니라 질문의 id만 저장된다 → answer 테이블에 question_id 컬럼(외래 키)이 생긴다.
    // 컬럼 이름 question_id = 필드 이름(question) + "_" + 상대 엔티티의 @Id 이름(id).
    // 자바 코드에서는 answer.question 으로 Question 객체를 바로 쓸 수 있고, JPA가 id ↔ 객체 변환을 알아서 해준다.
    // 빼면 JPA는 Question 타입을 어떻게 저장할지 몰라서 앱 시작 시 에러가 난다.
    // @ManyToOne: "many answers belong to one question".
    // The database stores only the question's id, not the whole Question → the answer table gets a question_id column (a foreign key).
    // Column name question_id = field name (question) + "_" + the other entity's @Id name (id).
    // In Java you use answer.question as a real Question object; JPA converts between the id and the object for you.
    // Without it, JPA doesn't know how to store a Question-typed field and the app fails at startup.
    @ManyToOne
    private Question question;

    // 답변 내용. 길이 제한 없는 TEXT 타입.
    // The answer text. TEXT type, no length limit.
    @Column(columnDefinition = "TEXT")
    private String content;
}
