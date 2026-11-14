// 도메인별 패키지 (14163 26강): 질문과 관련된 클래스(엔티티, 리포지터리, 앞으로 만들 컨트롤러·서비스)는 com.back.question 에,
// 답변 관련 클래스는 com.back.answer 에 모은다. 기능이 늘어도 "질문 기능은 question 폴더"처럼 찾기 쉽다.
// 다른 패키지의 클래스를 쓰려면 import 해야 한다 (아래 com.back.answer.Answer).
// 하위 패키지도 com.back 아래라서 @SpringBootApplication 의 컴포넌트 스캔 대상이다.
// Packages by domain (14163 lecture 26): question-related classes (entity, repository, and later the controller/service) live in com.back.question,
// answer-related ones in com.back.answer. As features grow, "question code is in the question folder" stays easy to find.
// Using a class from another package requires an import (com.back.answer.Answer below).
// Sub-packages are still under com.back, so @SpringBootApplication's component scan still finds them.
package com.back.question;

import com.back.answer.Answer;
import com.back.global.jpa.entity.BaseEntity;
import com.back.user.SiteUser;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static jakarta.persistence.FetchType.LAZY;

// @Entity: 이 클래스를 DB 테이블과 연결한다. 클래스 이름 Question → 테이블 이름 question.
// ddl-auto: update 설정 때문에 앱이 시작될 때 Hibernate가 이 클래스를 보고 테이블을 자동으로 만든다.
// 이 클래스의 객체 하나 = 테이블의 행(row) 하나.
// 빼면 그냥 평범한 자바 클래스가 되어 테이블이 만들어지지 않는다.
// @Entity: links this class to a database table. Class name Question → table name question.
// Because of ddl-auto: update, Hibernate reads this class at startup and creates the table automatically.
// One object of this class = one row in the table.
// Without it, this is just a plain Java class and no table is created.
@Entity
// @Getter / @Setter (롬복): 모든 필드의 getter(getSubject())와 setter(setSubject(...))를 컴파일할 때 자동으로 만들어준다.
// 필드가 private 이라서 다른 클래스(테스트, 샘플 데이터 등)가 값을 읽고 쓰려면 이 메서드들이 필요하다.
// 빼면 question.setSubject("...") 같은 코드가 "메서드를 찾을 수 없다"는 컴파일 오류가 난다.
// @Getter / @Setter (Lombok): generates a getter (getSubject()) and setter (setSubject(...)) for every field at compile time.
// The fields are private, so other classes (tests, sample data, ...) need these methods to read and write values.
// Without them, code like question.setSubject("...") fails to compile with "cannot find symbol".
@Getter
@Setter
// extends BaseEntity: id(@Id, 자동 번호), createDate(생성일), modifyDate(수정일)를 부모 클래스에서 물려받는다 (010-02).
// 예전에 여기 직접 있던 id·createDate 필드는 BaseEntity 로 옮겼다. 날짜는 JPA Auditing 이 자동으로 채운다.
// extends BaseEntity: inherits id (@Id, auto-numbered), createDate and modifyDate from the parent class (010-02).
// The id and createDate fields that used to live here moved to BaseEntity. JPA Auditing fills in the dates automatically.
public class Question extends BaseEntity {

    // @Column(length = 200): 제목은 최대 200자 → VARCHAR(200). 따로 적지 않으면 기본 길이는 255다.
    // @Column(length = 200): subject is at most 200 characters → VARCHAR(200). The default length is 255.
    @Column(length = 200)
    private String subject;

    // columnDefinition = "TEXT": 내용은 길이 제한 없는 TEXT 타입으로 만든다. 질문 본문은 길 수 있기 때문.
    // columnDefinition = "TEXT": content becomes a TEXT column with no length limit, because a question body can be long.
    @Column(columnDefinition = "TEXT")
    private String content;

    // 글쓴이 (교재 3-08). 여러 질문이 한 회원에게 속한다 → @ManyToOne, question 테이블에 author_id 컬럼(외래 키).
    // LAZY: 질문을 읽을 때 회원은 필요할 때만 읽는다 (009-03).
    // 글쓴이 기능 전에 만든 질문은 author 가 비어 있다(null) → 화면에서 null 을 확인하고 출력한다.
    // The author (book 3-08). Many questions belong to one member → @ManyToOne, an author_id column (foreign key) in the question table.
    // LAZY: the member is read only when needed (009-03).
    // Questions created before this feature have no author (null) → the templates check for null before printing.
    @ManyToOne(fetch = LAZY)
    private SiteUser author;

    // 추천한 회원들 (교재 3-10).
    // @ManyToMany: 한 질문을 여러 회원이 추천하고, 한 회원도 여러 질문을 추천할 수 있다 → 중간 테이블(question_voter)이 생긴다.
    // (005-01 의 중간 테이블은 실수였지만, 다대다 관계에서는 중간 테이블이 꼭 필요하다.)
    // Set: 같은 회원이 두 번 들어가지 않는다 → 한 사람이 여러 번 눌러도 추천 수는 1 만 올라간다.
    // The members who recommended this (book 3-10).
    // @ManyToMany: one question can be recommended by many members, and one member can recommend many questions → a join table (question_voter) is created.
    // (The join table in 005-01 was a mistake, but a many-to-many relationship genuinely needs one.)
    // Set: the same member can't be in it twice → pressing it many times raises the count by only 1.
    @ManyToMany
    private Set<SiteUser> voter = new HashSet<>();

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
    // cascade = PERSIST: 질문을 저장(save)할 때 answers 목록에 새로 들어간 답변도 함께 저장(INSERT)한다.
    // 그래서 answerRepository 없이 question.addAnswer(...) 만으로 답변을 만들 수 있다 (14163 21강).
    // cascade = REMOVE: deleting a question also deletes its answers.
    // Without it, the answers still point to the question (foreign key), so the database refuses to delete the question.
    // cascade = PERSIST: saving a question also saves (INSERTs) new answers added to its answers list.
    // So answers can be created with just question.addAnswer(...), without answerRepository (14163 lecture 21).
    @OneToMany(mappedBy = "question", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    // List<Answer>: 여러 개의 Answer 를 순서대로 담는 자바 컬렉션.
    // = new ArrayList<>(): 처음부터 빈 목록으로 만들어 둔다. 그러지 않으면 새 질문의 answers 가 null 이라서
    // answers.add(...) 를 하는 순간 NullPointerException 이 난다 (14163 23강).
    // List<Answer>: a Java collection holding many Answer objects in order.
    // = new ArrayList<>(): start with an empty list. Otherwise a new question's answers is null,
    // and answers.add(...) throws a NullPointerException (14163 lecture 23).
    private List<Answer> answers = new ArrayList<>();

    // 이 질문에 답변을 하나 추가하는 편의 메서드.
    // 연관관계의 양쪽(answer.question 과 question.answers)을 한 번에 맞춰 준다:
    // - answer.setQuestion(this): DB에 저장될 외래 키(question_id)를 정한다 (연관관계의 주인 쪽).
    // - answers.add(answer):      자바 객체의 목록에도 넣어서, 저장 전에도 question.getAnswers() 에 바로 보이게 한다.
    // A convenience method that adds one answer to this question.
    // It keeps both sides of the relationship in sync at once:
    // - answer.setQuestion(this): sets the foreign key (question_id) that gets saved (the owner side).
    // - answers.add(answer):      also adds it to the Java list, so question.getAnswers() shows it even before saving.
    public Answer addAnswer(String content) {
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setQuestion(this);
        // createDate 는 저장될 때 JPA Auditing 이 자동으로 넣는다 (BaseEntity).
        // createDate is filled in automatically by JPA Auditing when saved (BaseEntity).
        answers.add(answer);
        return answer;
    }
}
