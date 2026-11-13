package com.back;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// 리포지터리: 엔티티를 DB에 저장하고 꺼내는 일을 맡는 객체. SQL을 직접 쓰지 않아도 된다.
// 클래스가 아니라 "인터페이스"이고 안에 코드가 하나도 없다. 그런데도 동작하는 이유:
// 앱이 시작될 때 스프링 데이터 JPA가 JpaRepository 를 상속한 인터페이스를 찾아서 구현 객체를 자동으로 만들어준다.
// A repository: the object responsible for saving entities to the database and reading them back, without writing SQL.
// It's an interface, not a class, and it has no code inside. It still works because
// Spring Data JPA finds interfaces that extend JpaRepository at startup and generates an implementation automatically.
//
// JpaRepository<Question, Integer>:
// - Question = 이 리포지터리가 다루는 엔티티
// - Integer  = 그 엔티티의 @Id 타입 (Question.id 는 int 지만, 제네릭에는 기본형을 못 쓰므로 Integer 로 적는다)
// - Question = the entity this repository handles
// - Integer  = the type of that entity's @Id (Question.id is an int, but generics can't use primitives, so we write Integer)
//
// 상속만으로 공짜로 생기는 메서드 예시 / Methods you get for free just by extending it:
// save(q)      → INSERT (새 객체) 또는 UPDATE (이미 저장된 객체) / INSERT (new object) or UPDATE (already saved)
// findAll()    → SELECT * FROM question
// findById(id) → SELECT ... WHERE id = ?
// count()      → SELECT COUNT(*) FROM question
// delete(q)    → DELETE FROM question WHERE id = ?
public interface QuestionRepository extends JpaRepository<Question, Integer> {

    // ── 쿼리 메서드 (Query methods) ──────────────────────────────────────────────
    // 메서드 몸체 없이 "이름"만 정해진 규칙대로 지으면, 스프링 데이터 JPA가 이름을 해석해서 SQL을 만들어 준다.
    //   find + By + 필드이름  →  WHERE 필드 = ?
    // 이름에 오타가 있으면(예: findBySubjet) 앱이 시작될 때 "subjet 이라는 필드가 없다"는 오류로 바로 멈춘다.
    // Declare a method with no body, name it by the rules, and Spring Data JPA parses the name and builds the SQL.
    //   find + By + fieldName  →  WHERE field = ?
    // A typo in the name (e.g. findBySubjet) stops the app at startup with "no property subjet found".

    // findBySubject → WHERE subject = ?
    // 교재는 Question 을 그대로 반환해서 결과가 없으면 null 이 된다.
    // 강사 방식대로 Optional 로 감싸서, findById 처럼 "없을 수도 있다"는 것을 타입으로 드러낸다.
    // The book returns a plain Question, which becomes null when nothing matches.
    // Following the instructor, we wrap it in Optional so the type says "this might be missing", just like findById.
    Optional<Question> findBySubject(String subject);

    // findBySubjectAndContent → WHERE subject = ? AND content = ?
    // 매개변수는 이름에 나온 순서(Subject, Content)대로 SQL의 ? 에 채워진다.
    // The parameters fill the ? placeholders in the same order as in the name (Subject, then Content).
    Optional<Question> findBySubjectAndContent(String subject, String content);
}
