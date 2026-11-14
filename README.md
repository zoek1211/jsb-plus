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

### 002-01 메인 컨트롤러, 액션 메서드 2개 추가
- [링크](https://github.com/zoek1211/jsb-plus/releases/tag/002-01)
- 작업: `MainController`에 `/`, `/hello` 를 처리하는 `void` 액션 메서드 추가 (콘솔 출력만)
- 이유: 14163 3강 방식대로 `@ResponseBody` 없이 먼저 실행해 보고, 메서드 호출과 응답 생성이 별개라는 것을 확인하기 위함
- 결과: 콘솔에 `index() 메서드 호출됨!`, `hello() 메서드 호출됨!` 출력 확인, 브라우저는 500 에러(Circular view path) 응답

### 000-03 AI 에이전트 스킬 설정, 학습용 주석 규칙 추가
- [링크](https://github.com/zoek1211/jsb-plus/releases/tag/000-03)
- 작업: `AGENTS.md`에 학습용 주석 규칙(한국어+영어) 추가, 기존 코드 파일 5개에 주석 작성
- 이유: 코드를 읽으면서 각 줄이 무엇을, 왜 하는지 바로 이해하기 위함
- 결과: `./gradlew test` 통과, 동작 변경 없음 (주석만 추가)

### 002-02 메인 컨트롤러, @ResponseBody로 응답 본문 반환
- [링크](https://github.com/zoek1211/jsb-plus/releases/tag/002-02)
- 작업: 액션 메서드에 `@ResponseBody` 추가, `String` 반환 (`/` → `SBB`, `/hello` → `안녕하세요 ^ ^`), `MainControllerTest`(MockMvc) 추가
- 이유: 14163 4강 — 반환값이 템플릿 이름이 아니라 응답 본문이 되도록 하여 002-01의 500 에러 해결
- 결과: 테스트 3개 통과, 브라우저에서 `SBB`, `안녕하세요 ^ ^` 표시 확인

### 003-01 데이터베이스 설정, H2 추가 및 dev·test 프로필 분리
- [링크](https://github.com/zoek1211/jsb-plus/releases/tag/003-01)
- 작업: H2·H2 콘솔 의존성 추가, `application.properties` → `application.yaml` 전환, `application-dev.yaml`(파일 DB)·`application-test.yaml`(메모리 DB) 추가, 테스트에 `@ActiveProfiles("test")`, DB 파일 `.gitignore` 처리
- 이유: 14163 5강 — 교재처럼 DB 파일을 직접 만들지 않고, 개발 데이터와 테스트 데이터를 분리하기 위함
- 결과: 테스트 3개 통과(`test` 프로필), 앱은 `dev` 프로필로 실행, `/h2-console` 접속 확인. DB 파일은 아직 없음 (DB에 연결하는 코드가 없기 때문, 003-02 JPA 추가 후 생성)

### 000-04 AI 에이전트 스킬 설정, 범위 조정 및 교재 비교 문서 추가
- [링크](https://github.com/zoek1211/jsb-plus/releases/tag/000-04)
- 작업: `docs/book-vs-ours.md`(교재 vs 우리 비교표) 추가, `AGENTS.md`에 범위(1~3장)·영상 목표·비교표 갱신 규칙 추가
- 이유: 도전과제를 빼고 3장까지 구현한 뒤 영상을 만들기로 함. 교재와 달라진 점을 작업하면서 모아 두기 위함
- 결과: 지금까지의 차이 6가지 정리, 애플리케이션 코드 변경 없음

### 003-02 데이터베이스 설정, JPA 추가 및 SQL 로그 출력
- [링크](https://github.com/zoek1211/jsb-plus/releases/tag/003-02)
- 작업: `spring-boot-starter-data-jpa`(+ 테스트용) 의존성 추가, `ddl-auto: update`, SQL 출력(`show-sql`, `format_sql`, `highlight_sql`, `use_sql_comments`), 바인딩 값·트랜잭션 TRACE 로그 설정
- 이유: 14163 6강 — 자바 객체로 DB를 다루기 위한 준비, 실행되는 SQL과 실제 값까지 눈으로 확인하기 위함
- 결과: 테스트 3개 통과(메모리 DB `db_test` 연결 확인), 앱 실행 시 `db_dev.mv.db` 생성, H2 콘솔로 `jdbc:h2:./db_dev` 접속 성공 (테이블은 아직 없음)

### 004-01 엔티티, Question 엔티티 추가
- [링크](https://github.com/zoek1211/jsb-plus/releases/tag/004-01)
- 작업: `Question` 엔티티 추가 (`id`, `createDate`, `subject`(200자), `content`(TEXT))
- 이유: 14163 7강 — 질문 데이터를 저장할 테이블을 자바 클래스로 정의. 교재와 달리 답변 목록(`@OneToMany`)·getter/setter는 아직 넣지 않고 하나씩 추가
- 결과: 테스트 3개 통과, 로그에서 `create table question` 확인, H2 콘솔에서 `QUESTION` 테이블(ID, CONTENT, CREATE_DATE, SUBJECT) 확인

### 004-02 엔티티, Answer 엔티티 추가 (@ManyToOne)
- [링크](https://github.com/zoek1211/jsb-plus/releases/tag/004-02)
- 작업: `Answer` 엔티티 추가 (`id`, `createDate`, `content`(TEXT), `@ManyToOne Question question`)
- 이유: 14163 7강 — 답변이 어느 질문에 속하는지 저장. 먼저 `@ManyToOne` 단방향만 만들고 `@OneToMany`는 이후 강의에서 비교하며 추가
- 결과: 테스트 3개 통과, 로그에서 `question_id` 컬럼과 외래 키 제약조건 확인, H2 콘솔에서 `ANSWER` 테이블(ID, CONTENT, CREATE_DATE, QUESTION_ID) 확인

### 005-01 엔티티 관계, @OneToMany 추가 (mappedBy 없이 실험)
- [링크](https://github.com/zoek1211/jsb-plus/releases/tag/005-01)
- 작업: `Question`에 `@OneToMany(cascade = REMOVE) List<Answer> answers` 추가 — 일부러 `mappedBy` 생략
- 이유: 14163 11강 — `mappedBy`가 없으면 어떤 일이 생기는지 직접 확인
- 결과: 테스트 3개 통과, 로그에서 `create table question_answers (question_id, answers_id)` 확인, H2 콘솔에 불필요한 `QUESTION_ANSWERS` 중간 테이블 생성 확인 (005-02에서 수정)

### 005-02 엔티티 관계, @OneToMany에 mappedBy 추가
- [링크](https://github.com/zoek1211/jsb-plus/releases/tag/005-02)
- 작업: `@OneToMany(mappedBy = "question", cascade = REMOVE)` 로 수정, 개발 DB에 남은 `QUESTION_ANSWERS` 테이블을 H2 콘솔에서 직접 `DROP TABLE`
- 이유: 14163 12강 — 답변 목록이 `answer.question_id` 를 재사용하도록 하여 중간 테이블 제거. `ddl-auto: update` 는 테이블을 지우지 않으므로 직접 정리
- 결과: 테스트 3개 통과, 테스트 로그에는 `answer`, `question` 테이블만 생성, 개발 DB에도 `ANSWER`, `QUESTION` 만 남음 확인

### 006-01 리포지터리, QuestionRepository 추가
- [링크](https://github.com/zoek1211/jsb-plus/releases/tag/006-01)
- 작업: `QuestionRepository extends JpaRepository<Question, Integer>` 추가, `Question`에 롬복 `@Getter @Setter` 추가
- 이유: 14163 14강 — SQL 없이 질문을 저장·조회하기 위한 준비, private 필드를 다른 클래스에서 읽고 쓰기 위함
- 결과: 테스트 3개 통과, 로그에서 `Found 1 JPA repository interface` 확인, 컴파일된 클래스에서 getter/setter 10개 생성 확인

### 006-02 리포지터리, 테스트용 샘플 데이터(TestInitData) 추가
- [링크](https://github.com/zoek1211/jsb-plus/releases/tag/006-02)
- 작업: `@Profile("test")` 설정 클래스 `TestInitData` 추가, `ApplicationRunner`로 앱 시작 직후 샘플 질문 2개 저장 (`self` 프록시 + `@Lazy` + `@Transactional`)
- 이유: 14163 14강 — 테스트마다 데이터를 만들지 않고, 항상 같은 샘플 데이터에서 테스트를 시작하기 위함. 개발 DB에는 넣지 않음
- 결과: 테스트 3개 통과, 로그에서 `TestInitData$$SpringCGLIB$$0.work1` 트랜잭션·`insert` 2건·바인딩 값 확인, 개발 DB `QUESTION` 행 수 0 확인

### 006-03 리포지터리, QuestionRepositoryTest 추가 (findAll)
- [링크](https://github.com/zoek1211/jsb-plus/releases/tag/006-03)
- 작업: `QuestionRepositoryTest` 추가, `findAll()` 결과가 2개이고 첫 질문 제목이 `sbb가 무엇인가요?` 인지 검사
- 이유: 14163 14~15강 — 리포지터리 동작을 자동 테스트로 확인. 교재처럼 `SbbApplicationTests`에 몰아넣지 않고 검사 대상별 테스트 클래스로 분리
- 결과: 테스트 4개 통과, 로그에서 `select ... from question` 확인. 기대값을 3으로 바꾸면 `expected: <3> but was: <2>` 로 실패하는 것도 확인 후 원복

### 007-01 리포지터리, findById 테스트 추가
- [링크](https://github.com/zoek1211/jsb-plus/releases/tag/007-01)
- 작업: `QuestionRepositoryTest.t2` 추가 — `findById(1).orElseThrow()` 로 조회 후 제목 검사
- 이유: 14163 15강 — id로 질문 하나 조회. 교재·강사의 `if (oq.isPresent())` 방식은 값이 없을 때 검사를 건너뛰어 테스트가 통과해 버리므로 `orElseThrow()` 로 개선
- 결과: 테스트 5개 통과. `findById(99)` 로 바꿔 보니 교재 방식은 통과(가짜 초록불), 우리 방식은 `NoSuchElementException: No value present` 로 실패 확인 후 원복

### 007-02 리포지터리, 테스트 검사 방식을 AssertJ로 변경
- [링크](https://github.com/zoek1211/jsb-plus/releases/tag/007-02)
- 작업: `QuestionRepositoryTest` 의 JUnit `assertEquals` → AssertJ `assertThat(...).hasSize(...)`, `isEqualTo(...)` 로 변경
- 이유: 14163 16강 — 문장처럼 읽히고, 기대값·실제값 순서를 헷갈릴 일이 없으며, 실패 메시지에 목록 내용까지 나옴
- 결과: 테스트 5개 통과. `hasSize(3)` 으로 바꿔 보니 `Expected size: 3 but was: 2 in: [...]` 로 실패하는 것 확인 후 원복

### 007-03 리포지터리, findBySubject·findBySubjectAndContent 쿼리 메서드 추가
- [링크](https://github.com/zoek1211/jsb-plus/releases/tag/007-03)
- 작업: `QuestionRepository`에 `Optional<Question> findBySubject`, `findBySubjectAndContent` 추가, 테스트 t3·t4 추가
- 이유: 14163 16강 — 메서드 이름만으로 SQL을 만드는 쿼리 메서드 학습. 교재와 달리 결과 없음을 `null` 이 아닌 `Optional` 로 표현
- 결과: 테스트 7개 통과, 로그에서 `where q1_0.subject=?`, `where q1_0.subject=? and q1_0.content=?` 와 바인딩 값 순서 확인

### 007-04 리포지터리, findBySubjectLike 쿼리 메서드 추가
- [링크](https://github.com/zoek1211/jsb-plus/releases/tag/007-04)
- 작업: `List<Question> findBySubjectLike(String subjectLike)` 추가, 테스트 t5 추가 (`"sbb%"` 로 조회 → 1개, 제목 확인)
- 이유: 14163 17강 — 패턴 검색(`LIKE`, `%`) 학습. 여러 개가 나올 수 있어 `List` 반환. 교재와 달리 결과 개수까지 검사해 다른 질문이 걸러졌는지 확인
- 결과: 테스트 8개 통과, 로그에서 `where q1_0.subject like ? escape '\'` 와 바인딩 값 `sbb%` 확인

### 008-01 리포지터리, 데이터 수정 테스트 추가 (@Transactional 롤백)
- [링크](https://github.com/zoek1211/jsb-plus/releases/tag/008-01)
- 작업: `QuestionRepositoryTest.t6` 추가 — 질문 1번 제목을 "수정된 제목"으로 바꿔 `save`, 바뀐 제목으로 다시 찾아 id 1 확인. 테스트 메서드에 `@Transactional`
- 이유: 14163 18강 — 데이터를 바꾸는 테스트가 다른 테스트에 영향을 주지 않도록, 테스트가 끝나면 롤백
- 결과: 테스트 9개 통과, 로그에서 `update question set ... where id=?` 확인. `@Transactional` 없이 수정 테스트를 먼저 실행하면 나머지 5개가 모두 실패, 붙이면 먼저 실행해도 모두 통과하는 것 확인 (시연 코드는 원복)

### 008-02 리포지터리, 데이터 삭제 테스트 추가
- [링크](https://github.com/zoek1211/jsb-plus/releases/tag/008-02)
- 작업: `QuestionRepositoryTest.t7` 추가 — `count()` 2 확인 → 질문 1번 `delete` → `count()` 1 확인, `@Transactional` 로 롤백
- 이유: 14163 19강 — 리포지터리로 데이터를 삭제하는 방법 학습, 수정 테스트와 같은 이유로 롤백해 다른 테스트에 영향 없음
- 결과: 테스트 10개 통과, 로그에서 `delete from question where id=?` 확인

### 000-05 AI 에이전트 스킬 설정, README에 핵심 변경·실행화면 추가 규칙
- [링크](https://github.com/zoek1211/jsb-plus/releases/tag/000-05)
- 작업: `AGENTS.md` 의 README 작업내역 형식에 `핵심 변경`(코드 몇 줄)과 `실행화면`(캡처 이미지) 항목 추가
- 이유: 각 커밋에서 무엇이 바뀌었고 실제로 어떻게 동작했는지 README만 보고 알 수 있도록 하기 위함 (영상 제작 자료)
- 결과: 문서만 변경, 애플리케이션 코드 변경 없음
- 핵심 변경:
  ```markdown
  - 핵심 변경: (코드 블록)
  - 실행화면: ![커밋번호 실행화면](docs/images/커밋번호.jpg)
  ```
- 실행화면: 해당 없음 (문서 변경)
