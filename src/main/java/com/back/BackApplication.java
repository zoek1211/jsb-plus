package com.back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// 스프링부트 앱의 시작점임을 알린다. 세 가지 일을 한다:
// 1) com.back 패키지와 그 하위 패키지에서 @Controller 같은 클래스를 찾아 객체로 만든다 (컴포넌트 스캔).
// 2) 추가된 라이브러리를 보고 웹 서버 같은 설정을 자동으로 한다 (자동 설정).
// 3) 이 클래스 자체도 설정 클래스로 쓴다.
// 그래서 컨트롤러는 반드시 com.back 아래에 있어야 스프링이 찾을 수 있다.
//
// Marks the starting point of the Spring Boot app. It does three things:
// 1) Finds classes like @Controller in com.back and its sub-packages and creates objects from them (component scan).
// 2) Configures things like the web server automatically, based on the libraries added (auto-configuration).
// 3) Uses this class itself as a configuration class.
// That's why controllers must live under com.back for Spring to find them.
@SpringBootApplication
// @EnableJpaAuditing: JPA Auditing 기능을 켠다. BaseEntity 의 @CreatedDate·@LastModifiedDate 에 시간이 자동으로 들어간다.
// 빼면 두 날짜가 항상 null 로 저장된다.
// @EnableJpaAuditing: turns on JPA Auditing, so BaseEntity's @CreatedDate and @LastModifiedDate get filled automatically.
// Without it, both dates are always saved as null.
@EnableJpaAuditing
public class BackApplication {

	// 자바 프로그램은 main 메서드에서 시작한다.
	// A Java program starts from its main method.
	public static void main(String[] args) {
		// 스프링을 실행한다: 객체들을 만들고, 내장 Tomcat 웹 서버를 8080 포트로 띄운다.
		// Starts Spring: creates the objects and launches the built-in Tomcat web server on port 8080.
		SpringApplication.run(BackApplication.class, args);
	}

}
