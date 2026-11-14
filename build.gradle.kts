// Gradle 빌드 설정 파일 (Kotlin DSL). 프로젝트를 어떻게 컴파일·실행·테스트할지 정의한다.
// Gradle build file (Kotlin DSL). Defines how the project is compiled, run, and tested.

plugins {
	// 자바 컴파일, 테스트, jar 만들기 같은 기본 기능을 켠다.
	// Turns on basic Java support: compiling, testing, building a jar.
	java
	// bootRun, bootJar 같은 스프링부트 전용 작업을 추가한다.
	// Adds Spring Boot tasks such as bootRun and bootJar.
	id("org.springframework.boot") version "4.1.2"
	// 스프링부트 버전에 맞는 라이브러리 버전을 자동으로 골라준다. 그래서 아래 dependencies에 버전을 안 적어도 된다.
	// Picks library versions that match this Spring Boot version, so dependencies below need no version numbers.
	id("io.spring.dependency-management") version "1.1.7"
}

// 이 프로젝트의 그룹(조직)과 버전. 지금은 크게 신경 쓰지 않아도 된다.
// The project's group (organization) and version. Not important for now.
group = "com"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		// 이 프로젝트는 Java 25로 컴파일한다. 설치돼 있지 않으면 settings.gradle.kts의 foojay 플러그인이 내려받는다.
		// Compile with Java 25. If it isn't installed, the foojay plugin in settings.gradle.kts downloads it.
		languageVersion = JavaLanguageVersion.of(25)
	}
}

repositories {
	// 라이브러리를 내려받을 곳. Maven Central은 자바 라이브러리의 공용 저장소다.
	// Where libraries are downloaded from. Maven Central is the public library store for Java.
	mavenCentral()
}

dependencies {
	// implementation: 앱을 실행할 때 필요한 라이브러리. webmvc 스타터 = 웹 서버(Tomcat) + 스프링 MVC(컨트롤러 등).
	// implementation: needed when the app runs. The webmvc starter = web server (Tomcat) + Spring MVC (controllers etc).
	implementation("org.springframework.boot:spring-boot-starter-webmvc")

	// 롬복: getter, 생성자 같은 반복 코드를 컴파일할 때 자동으로 만들어준다.
	// compileOnly = 컴파일할 때만 필요, annotationProcessor = 컴파일 중에 코드를 생성하는 도구로 사용.
	// Lombok: generates repetitive code (getters, constructors) at compile time.
	// compileOnly = needed only to compile; annotationProcessor = runs during compilation to generate code.
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	// 개발 도구: 코드를 바꾸면 앱을 자동으로 재시작한다. developmentOnly라서 배포용 jar에는 들어가지 않는다.
	// DevTools: restarts the app automatically when code changes. developmentOnly = left out of the production jar.
	developmentOnly("org.springframework.boot:spring-boot-devtools")

	// H2: 자바로 만든 가벼운 데이터베이스. 따로 설치할 필요 없이 앱 안에서 같이 실행된다.
	// runtimeOnly = 코드에서 직접 쓰지 않고, 앱이 실행될 때만 필요하다 (스프링이 알아서 연결).
	// H2: a lightweight database written in Java. It runs inside the app, no separate install needed.
	// runtimeOnly = our code never uses it directly; it's only needed while the app runs (Spring connects to it).
	runtimeOnly("com.h2database:h2")
	// H2 콘솔: 브라우저(/h2-console)에서 DB 안을 들여다볼 수 있는 웹 화면.
	// H2 console: a web page (/h2-console) for looking inside the database from the browser.
	implementation("org.springframework.boot:spring-boot-h2console")

	// JPA(Java Persistence API): SQL을 직접 쓰지 않고 자바 객체로 DB를 다루게 해주는 표준. 구현체는 Hibernate.
	// 이 스타터가 DB 연결(DataSource)도 함께 만들어서, 앱이 시작될 때 실제로 DB에 연결한다 → db_dev 파일이 생긴다.
	// JPA (Java Persistence API): a standard for working with the database through Java objects instead of writing SQL. Hibernate implements it.
	// This starter also creates the database connection (DataSource), so the app actually connects on startup → the db_dev file is created.
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	// 타임리프: HTML 템플릿 파일(src/main/resources/templates/*.html)에 자바 데이터를 채워 화면을 만드는 템플릿 엔진.
	// 컨트롤러가 "question_list" 를 반환하면 templates/question_list.html 을 찾아서 렌더링한다.
	// Thymeleaf: a template engine that fills HTML template files (src/main/resources/templates/*.html) with Java data.
	// When a controller returns "question_list", it renders templates/question_list.html.
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	testImplementation("org.springframework.boot:spring-boot-starter-thymeleaf-test")
	// JPA 관련 테스트 도구 (나중에 리포지터리 테스트에서 사용).
	// Test tools for JPA (used later for repository tests).
	testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")

	// 테스트에서만 쓰는 라이브러리들 (JUnit, MockMvc 등).
	// Libraries used only by tests (JUnit, MockMvc, etc).
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testCompileOnly("org.projectlombok:lombok")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testAnnotationProcessor("org.projectlombok:lombok")
}

tasks.withType<Test> {
	// 테스트를 JUnit 5(JUnit Platform)로 실행한다.
	// Run tests with JUnit 5 (the JUnit Platform).
	useJUnitPlatform()
}
