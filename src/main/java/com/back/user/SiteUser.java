package com.back.user;

import com.back.global.jpa.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

// 회원 엔티티 → site_user 테이블 (교재 3-06).
// 이름이 User 가 아닌 이유: user 는 H2 등 여러 DB의 예약어라 테이블 이름으로 쓰면 오류가 날 수 있다. 시큐리티의 User 클래스와도 겹친다.
// The member entity → site_user table (book 3-06).
// Why not "User": user is a reserved word in several databases (H2 included) and can break as a table name. It also clashes with Security's User class.
@Entity
@Getter
@Setter
// id, createDate, modifyDate 는 BaseEntity 에서 (교재의 SiteUser 는 id 만 직접 선언).
// id, createDate and modifyDate come from BaseEntity (the book's SiteUser declares only id itself).
public class SiteUser extends BaseEntity {
    // unique = true: 같은 값이 두 번 저장되지 못하게 DB에 "유일" 제약을 건다. 중복이면 저장 시 DataIntegrityViolationException.
    // unique = true: adds a database "unique" constraint so the same value can't be stored twice. A duplicate throws DataIntegrityViolationException on save.
    @Column(unique = true)
    private String username;

    // 암호화된(BCrypt) 비밀번호가 저장된다. 원래 비밀번호는 어디에도 저장하지 않는다.
    // Stores the encrypted (BCrypt) password. The original password is never stored anywhere.
    private String password;

    @Column(unique = true)
    private String email;
}
