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
| JPA·SQL 로그 | `hibernate.dialect` 직접 지정, SQL 출력 설정은 나중에 별도로 | dialect 자동 감지, 처음부터 SQL 정렬·색상·주석 + 바인딩 값(`?` 에 들어간 실제 값)·트랜잭션 TRACE 로그 | 설정을 줄이고, JPA가 실제로 실행하는 SQL과 값을 처음부터 확인 (14163 6강) | [003-02](https://github.com/zoek1211/jsb-plus/releases/tag/003-02) |
| 엔티티 작성 순서 | `Question`·`Answer`를 한 번에, 처음부터 양방향(`@OneToMany(mappedBy)` + `@ManyToOne`)과 `@Getter @Setter` | 한 클래스씩 추가, 처음엔 `@ManyToOne` 단방향만, 필요할 때 getter/setter 추가, `id`는 `int` | 어노테이션 하나가 테이블을 어떻게 바꾸는지 단계별로 확인 (14163 7~12강) | [004-01](https://github.com/zoek1211/jsb-plus/releases/tag/004-01), [004-02](https://github.com/zoek1211/jsb-plus/releases/tag/004-02) |
| `@OneToMany` 와 `mappedBy` | 처음부터 `@OneToMany(mappedBy = "question")` | 먼저 `mappedBy` 없이 추가해 불필요한 중간 테이블(`question_answers`)이 생기는 것을 확인 → 이후 `mappedBy` 추가 | `mappedBy`가 왜 필요한지 결과로 확인 (14163 11~12강) | [005-01](https://github.com/zoek1211/jsb-plus/releases/tag/005-01), [005-02](https://github.com/zoek1211/jsb-plus/releases/tag/005-02) |
| 샘플 데이터 위치 | 테스트 메서드 안에서 직접 `new Question()` + `save()` | `@Profile("test")` 인 `TestInitData`가 앱 시작 시 한 번만 저장 (`ApplicationRunner`, `self` 프록시로 `@Transactional` 적용) | 테스트는 검증만 하고, 항상 같은 데이터에서 시작. 개발 DB는 건드리지 않음 (14163 14강) | [006-02](https://github.com/zoek1211/jsb-plus/releases/tag/006-02) |
| 테스트 클래스 구성 | 모든 테스트를 `SbbApplicationTests` 한 클래스에 작성 | 검사 대상별로 분리 (`MainControllerTest`, `QuestionRepositoryTest`) | 테스트가 늘어나도 찾기 쉽고, 무엇을 검사하는지 이름으로 알 수 있음 (14163 15강) | [006-03](https://github.com/zoek1211/jsb-plus/releases/tag/006-03) |
| `Optional` 검사 | `if (oq.isPresent()) { assertEquals(...) }` — 값이 없으면 검사를 건너뛰어 테스트가 통과함 | `findById(1).orElseThrow()` — 값이 없으면 예외로 테스트 실패 | 데이터가 없을 때 "가짜 초록불"을 막음. `findById(99)` 로 두 방식 차이를 직접 확인 | [007-01](https://github.com/zoek1211/jsb-plus/releases/tag/007-01) |
| 테스트 검사(assertion) 스타일 | JUnit `assertEquals(기대값, 실제값)` | AssertJ `assertThat(실제값).isEqualTo(기대값)`, `hasSize(n)` | 문장처럼 읽히고 인자 순서 실수가 없으며, 실패 메시지가 더 자세함 (14163 16강) | [007-02](https://github.com/zoek1211/jsb-plus/releases/tag/007-02) |
| 쿼리 메서드 반환 타입 | `Question findBySubject(...)` — 결과가 없으면 `null` | `Optional<Question> findBySubject(...)` | "없을 수도 있음"을 타입으로 드러내 `NullPointerException` 예방, `findById`와 같은 방식 (14163 16강) | [007-03](https://github.com/zoek1211/jsb-plus/releases/tag/007-03) |
| LIKE 검색 테스트 | 첫 번째 결과의 제목만 확인 | 결과 개수(`hasSize(1)`)와 제목을 함께 확인 | 조건에 맞지 않는 질문이 실제로 걸러졌는지까지 보장 | [007-04](https://github.com/zoek1211/jsb-plus/releases/tag/007-04) |
| 수정·삭제 테스트 격리 | 수정 결과가 DB에 그대로 남음 → 다른 테스트가 실행 순서에 따라 실패할 수 있음 | 테스트 메서드에 `@Transactional` → 끝나면 자동 롤백, 모든 테스트가 같은 데이터에서 시작 | 실행 순서와 무관하게 테스트 결과가 같아야 함. 순서를 강제한 시연으로 5개 실패 → 0개 실패 확인 (14163 18~19강) | [008-01](https://github.com/zoek1211/jsb-plus/releases/tag/008-01), [008-02](https://github.com/zoek1211/jsb-plus/releases/tag/008-02) |
