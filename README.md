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

### 000-01 AI 에이전트 스킬 설정, 이슈 트래커·라벨·도메인 문서 구성
- [링크](https://github.com/zoek1211/jsb-plus/releases/tag/000-01)
- 작업: `AGENTS.md`에 과제 절대 규칙·커밋 규칙 정리, `docs/agents/`에 이슈 트래커·트리아지 라벨·도메인 문서 설정 추가, GitHub 라벨 생성
- 이유: AI 에이전트가 과제 규칙(작은 작업, 승인 후 진행, 커밋·README 형식)을 매번 자동으로 따르도록 하기 위함
- 결과: GitHub에 트리아지 라벨 5개 확인, 애플리케이션 코드 변경 없음

### 000-02 AI 에이전트 스킬 설정, CLAUDE.md에서 AGENTS.md 불러오기
- [링크](https://github.com/zoek1211/jsb-plus/releases/tag/000-02)
- 작업: `@AGENTS.md` 한 줄짜리 `CLAUDE.md` 추가
- 이유: Claude Code는 `CLAUDE.md`만 자동으로 읽으므로, 과제 규칙(`AGENTS.md`)이 매 세션 자동 적용되도록 하기 위함
- 결과: 규칙은 `AGENTS.md` 한 곳에서만 관리, 애플리케이션 코드 변경 없음
