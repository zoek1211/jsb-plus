package com.back;

import org.springframework.data.jpa.repository.JpaRepository;

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
}
