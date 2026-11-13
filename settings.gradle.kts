// Gradle이 빌드 파일(build.gradle.kts)보다 먼저 읽는 설정 파일.
// Settings file that Gradle reads before the build file (build.gradle.kts).

plugins {
	// 로컬에 JDK 25가 없으면 Gradle이 자동으로 내려받도록 해주는 플러그인. 이게 없으면 "Java 25를 찾을 수 없다"는 오류로 빌드가 실패한다.
	// Lets Gradle download JDK 25 automatically if it isn't installed. Without it, the build fails with "cannot find Java 25".
	id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

// 프로젝트 이름. 메인 클래스 이름(BackApplication)도 여기서 나왔다.
// The project name. The main class name (BackApplication) came from this.
rootProject.name = "back"
