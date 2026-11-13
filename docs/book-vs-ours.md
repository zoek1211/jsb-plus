# 교재 vs 우리 구현 (Book vs Ours)

『점프 투 스프링부트』 교재 방식과 이 프로젝트의 구현 방식(장희성 강사 14163·14168 개선 적용)의 차이를 정리한다.
영상 제작 시 "무엇이 바뀌었는가"의 근거 자료로 사용한다.

범위: 교재 1~3장 (도전과제 제외)

| 주제 | 교재 | 우리 | 이유 | 커밋 |
|---|---|---|---|---|
| 빌드 환경 | `build.gradle`(Groovy), Spring Boot 3, Java 17 | `build.gradle.kts`(Kotlin DSL), Spring Boot 4.1.2, Java 25 (foojay 자동 설치) | 강사의 최신 실습 환경과 일치 | [001-01](https://github.com/zoek1211/jsb-plus/releases/tag/001-01) |
| 패키지 | `com.mysite.sbb` | `com.back` | 강사 방식 | [001-01](https://github.com/zoek1211/jsb-plus/releases/tag/001-01) |
| 코드 주석 | 없음 | 모든 파일에 한국어+영어 학습용 주석 | 코드만 읽어도 학습 가능 | [000-03](https://github.com/zoek1211/jsb-plus/releases/tag/000-03) |
| 첫 컨트롤러 | 처음부터 `@ResponseBody` | `void` 메서드로 먼저 500 에러 확인 → `@ResponseBody` 추가 | 어노테이션이 왜 필요한지 직접 확인 (14163 3~4강) | [002-01](https://github.com/zoek1211/jsb-plus/releases/tag/002-01), [002-02](https://github.com/zoek1211/jsb-plus/releases/tag/002-02) |
| 테스트 | 컨트롤러 테스트 없음 | 첫 컨트롤러부터 MockMvc 테스트 | 동작을 자동으로 보장 | [002-02](https://github.com/zoek1211/jsb-plus/releases/tag/002-02) |
| DB 설정 | `~/local.mv.db` 파일을 직접 생성, 설정 파일 하나 | 프로젝트 폴더의 파일 DB, `dev`(파일)·`test`(메모리) 프로필 분리, YAML | 수동 작업 제거, 테스트가 개발 데이터를 건드리지 않음 (14163 5강) | [003-01](https://github.com/zoek1211/jsb-plus/releases/tag/003-01) |
