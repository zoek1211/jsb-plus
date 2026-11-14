package com.back.user;

import lombok.Getter;

// 회원 권한 (교재 3-07). enum: 정해진 몇 개의 값만 가질 수 있는 타입.
// 스프링 시큐리티는 권한 이름이 "ROLE_" 로 시작하는 것을 역할(role)로 인식한다.
// Member roles (book 3-07). enum: a type that can hold only a fixed set of values.
// Spring Security treats authority names starting with "ROLE_" as roles.
@Getter
public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    // 시큐리티에 넘겨줄 권한 문자열
    // The authority string handed to Security
    private final String value;

    UserRole(String value) {
        this.value = value;
    }
}
