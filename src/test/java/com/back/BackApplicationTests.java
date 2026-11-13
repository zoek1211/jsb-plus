package com.back;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

// "test" 프로필로 실행한다 → application-test.yaml(메모리 DB)이 적용된다.
// 빼면 application.yaml 의 기본값 "dev" 로 실행되어 테스트가 개발 DB 파일(db_dev)을 사용하게 된다.
// Runs with the "test" profile → application-test.yaml (in-memory database) is applied.
// Without it, the default "dev" profile from application.yaml is used and tests would touch the dev database file (db_dev).
@ActiveProfiles("test")
// 테스트할 때 실제 앱과 똑같이 스프링 전체를 띄운다.
// Starts the whole Spring app for the test, just like running it for real.
@SpringBootTest
class BackApplicationTests {

	// @Test: JUnit이 실행할 테스트 메서드라는 표시.
	// 내용이 비어 있어도 의미가 있다: 스프링이 오류 없이 시작되면 통과, 설정이 잘못돼 시작에 실패하면 실패.
	// @Test: marks a method for JUnit to run.
	// Empty on purpose: it passes if Spring starts without errors, and fails if a bad setup stops it from starting.
	@Test
	void contextLoads() {
	}

}
