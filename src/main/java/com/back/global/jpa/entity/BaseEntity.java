package com.back.global.jpa.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

// 모든 엔티티가 공통으로 갖는 필드(id, 생성일, 수정일)를 한 곳에 모은 부모 클래스 (14168 6강).
// Question, Answer 가 이 클래스를 상속(extends)하면 이 필드들을 각자 다시 쓰지 않아도 된다.
// The parent class holding the fields every entity shares (id, created date, modified date) (14168 lecture 6).
// When Question and Answer extend it, they don't have to repeat these fields.
//
// @MappedSuperclass: 이 클래스 자체로는 테이블을 만들지 않고, 필드만 자식 엔티티의 테이블(question, answer)에 컬럼으로 물려준다.
// @MappedSuperclass: this class gets no table of its own; its fields become columns in each child entity's table (question, answer).
@MappedSuperclass
// AuditingEntityListener: 엔티티가 저장·수정될 때 @CreatedDate, @LastModifiedDate 필드에 현재 시간을 자동으로 넣어준다.
// BackApplication 의 @EnableJpaAuditing 이 있어야 동작한다.
// AuditingEntityListener: automatically fills @CreatedDate and @LastModifiedDate with the current time when an entity is saved or updated.
// Requires @EnableJpaAuditing on BackApplication.
@EntityListeners(AuditingEntityListener.class)
// getter 만 만든다. id 와 날짜는 DB·JPA가 정하는 값이라 코드에서 직접 바꾸지 못하게 setter 는 두지 않는다.
// Only getters. id and the dates are set by the database/JPA, so there are no setters to change them by hand.
@Getter
// abstract: 단독으로 new BaseEntity() 할 일이 없는 "틀" 클래스라는 표시.
// abstract: marks it as a template class that's never created on its own (no new BaseEntity()).
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;

    // 처음 저장(INSERT)될 때의 시간. 교재처럼 setCreateDate(LocalDateTime.now()) 를 직접 부를 필요가 없다.
    // The time of the first save (INSERT). No need to call setCreateDate(LocalDateTime.now()) by hand like the book does.
    @CreatedDate
    private LocalDateTime createDate;

    // 마지막으로 수정(UPDATE)된 시간. 교재는 3-09(수정 기능)에서야 직접 추가하지만, 여기서 미리 자동으로 관리한다.
    // The time of the last update (UPDATE). The book adds this by hand only in 3-09 (edit feature); here it's managed automatically from the start.
    @LastModifiedDate
    private LocalDateTime modifyDate;
}
