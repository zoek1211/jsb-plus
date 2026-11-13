package com.back;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.time.LocalDateTime;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

// @Entity: 이 클래스를 DB 테이블과 연결한다. 클래스 이름 Question → 테이블 이름 question.
// ddl-auto: update 설정 때문에 앱이 시작될 때 Hibernate가 이 클래스를 보고 테이블을 자동으로 만든다.
// 이 클래스의 객체 하나 = 테이블의 행(row) 하나.
// 빼면 그냥 평범한 자바 클래스가 되어 테이블이 만들어지지 않는다.
// @Entity: links this class to a database table. Class name Question → table name question.
// Because of ddl-auto: update, Hibernate reads this class at startup and creates the table automatically.
// One object of this class = one row in the table.
// Without it, this is just a plain Java class and no table is created.
@Entity
public class Question {

    // @Id: 기본 키(primary key). 각 행을 구별하는 고유 번호. 엔티티에는 반드시 하나 있어야 한다.
    // @Id: the primary key, a unique number that identifies each row. Every entity must have one.
    @Id
    // @GeneratedValue(IDENTITY): 번호를 우리가 정하지 않고 DB가 1, 2, 3... 순서로 자동으로 매긴다 (AUTO_INCREMENT).
    // @GeneratedValue(IDENTITY): we don't pick the number; the database assigns 1, 2, 3... automatically (AUTO_INCREMENT).
    @GeneratedValue(strategy = IDENTITY)
    // int(기본형)는 null이 될 수 없어서, 아직 저장 전인 객체는 id가 0이다. 교재처럼 Integer를 쓰면 저장 전에는 null이다.
    // int (a primitive) can't be null, so an object not yet saved has id 0. With Integer (as in the book) it would be null.
    private int id;

    // 작성 날짜·시간. 자바의 camelCase 이름(createDate)은 DB에서 snake_case(create_date) 컬럼이 된다.
    // When it was written. The Java camelCase name (createDate) becomes a snake_case column (create_date) in the database.
    private LocalDateTime createDate;

    // @Column(length = 200): 제목은 최대 200자 → VARCHAR(200). 따로 적지 않으면 기본 길이는 255다.
    // @Column(length = 200): subject is at most 200 characters → VARCHAR(200). The default length is 255.
    @Column(length = 200)
    private String subject;

    // columnDefinition = "TEXT": 내용은 길이 제한 없는 TEXT 타입으로 만든다. 질문 본문은 길 수 있기 때문.
    // columnDefinition = "TEXT": content becomes a TEXT column with no length limit, because a question body can be long.
    @Column(columnDefinition = "TEXT")
    private String content;

    // @OneToMany: "하나(One)의 질문에 여러(Many) 답변" 관계. Answer 쪽 @ManyToOne 의 반대 방향이다.
    // 이 필드 덕분에 질문에서 답변 목록을 바로 꺼낼 수 있다 (question.answers).
    // @OneToMany: "one question has many answers". The reverse direction of @ManyToOne in Answer.
    // This field lets you get a question's answers directly (question.answers).
    //
    // mappedBy = "question": "이 목록은 Answer 클래스의 question 필드가 이미 저장하고 있는 관계의 반대편이다"라는 뜻.
    // 그래서 JPA는 새 테이블이나 컬럼을 만들지 않고, answer.question_id 를 이용해 답변 목록을 찾는다.
    // 관계를 실제로 저장하는 쪽(외래 키를 가진 Answer.question)을 "연관관계의 주인"이라고 부른다.
    // 빼면 JPA가 이것을 별개의 관계로 보고 중간 테이블(question_answers)을 따로 만든다 (005-01 에서 확인).
    // mappedBy = "question": "this list is the other side of the relationship already stored by Answer's question field".
    // So JPA creates no new table or column, and finds the answers through answer.question_id.
    // The side that actually stores the link (Answer.question, which holds the foreign key) is called the "owner" of the relationship.
    // Without it, JPA treats this as a separate relationship and creates a join table (question_answers), as seen in 005-01.
    //
    // cascade = REMOVE: 질문을 삭제하면 그 질문의 답변들도 함께 삭제한다.
    // 없으면 답변이 아직 질문을 가리키고 있어서(외래 키) DB가 질문 삭제를 거부한다.
    // cascade = REMOVE: deleting a question also deletes its answers.
    // Without it, the answers still point to the question (foreign key), so the database refuses to delete the question.
    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    // List<Answer>: 여러 개의 Answer 를 순서대로 담는 자바 컬렉션.
    // List<Answer>: a Java collection holding many Answer objects in order.
    private List<Answer> answers;
}
