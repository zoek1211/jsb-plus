package com.back.answer;

import com.back.global.jpa.entity.BaseEntity;
import com.back.question.Question;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.FetchType.LAZY;

// 답변 엔티티 → answer 테이블. @Column 의 뜻은 Question.java 와 같다.
// Answer entity → answer table. @Column means the same as in Question.java.
@Entity
// getter/setter 자동 생성 (자세한 설명은 Question.java 참고). 테스트·샘플 데이터에서 답변 값을 넣고 읽기 위해 필요하다.
// Generates getters/setters (see Question.java for details). Needed to set and read answer values in tests and sample data.
@Getter
@Setter
// id, createDate, modifyDate 는 BaseEntity 에서 물려받는다 (010-02).
// id, createDate and modifyDate are inherited from BaseEntity (010-02).
public class Answer extends BaseEntity {

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
    //
    // fetch = LAZY: 답변을 꺼낼 때 질문은 바로 읽지 않고, answer.getQuestion() 의 값을 실제로 쓸 때 읽는다.
    // @ManyToOne 의 기본값은 EAGER(항상 같이 읽기)라서, 필요 없는 질문까지 매번 조회하게 된다.
    // 강사 권장: "웬만하면 LAZY만 사용" (14163 24강). 대신 사용할 때 트랜잭션이 열려 있어야 한다.
    // fetch = LAZY: loading an answer doesn't read its question right away; it's read when answer.getQuestion()'s data is actually used.
    // @ManyToOne defaults to EAGER (always load together), which fetches the question every time even when it isn't needed.
    // The instructor's advice: "use LAZY whenever possible" (14163 lecture 24). In exchange, a transaction must be open when it's used.
    @ManyToOne(fetch = LAZY)
    private Question question;

    // 답변 내용. 길이 제한 없는 TEXT 타입.
    // The answer text. TEXT type, no length limit.
    @Column(columnDefinition = "TEXT")
    private String content;
}
