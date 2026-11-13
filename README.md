# jsb-plus

『점프 투 스프링부트』를 AI 코딩 에이전트와 함께 구현하고, 장희성 강사의 14163·14168 수업 내용을 적용하여 개선하는 프로젝트입니다.

## 개발 환경
- Java 25
- Spring Boot 4.1.2
- Gradle 9.8.2 (Kotlin DSL, `build.gradle.kts`)

## 참고 자료
- [점프 투 스프링부트 교재](https://wikidocs.net/book/7601)
- [14163 — 점프 투 스프링부트 개선](https://www.slog.gg/p/14163/raw)
- [14168 — JPA, 타임리프 개선](https://www.slog.gg/p/14168/raw)

## 작업내역

### 001-01 프로젝트 세팅, 스프링 이니셜라이저로 프로젝트 생성
- [링크](https://github.com/zoek1211/jsb-plus/releases/tag/001-01)
- 작업: Spring Initializr로 프로젝트 생성(Web, Lombok, DevTools), foojay 플러그인 추가, README 작성
- 이유: 강사 실습 환경(Spring Boot 4.1.2, Java 25, Gradle Kotlin DSL)과 동일하게 시작하기 위함
- 결과: `./gradlew test` 통과, 애플리케이션 8080 포트 정상 실행 확인
