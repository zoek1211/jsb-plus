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
