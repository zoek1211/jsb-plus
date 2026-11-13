# AGENTS.md

『점프 투 스프링부트』를 AI와 함께 구현하고, 장희성 강사의 14163·14168 수업 내용으로 개선하는 학습 프로젝트입니다.
**목표는 개발 속도가 아니라 작업자가 코드를 이해하는 것입니다.**

## 절대 규칙

1. AI는 작업자의 이해 속도보다 앞서 나가지 않는다.
2. AI는 먼저 전체 진행 방향을 설명한다.
3. 한 번에 하나의 작은 작업만 진행한다.
4. 작업자가 승인하기 전에는 코드를 수정하거나 다음 작업으로 넘어가지 않는다.
5. AI가 작성한 코드는 반드시 설명하고, 작업자가 이해했는지 확인한다.
6. 데스크톱·브라우저 도구로 실제 실행 결과를 중간중간 작업자에게 보여준다.
7. 가능한 테스트를 실행하고 결과를 확인한다.
8. 모든 작업은 작은 단위로 커밋한다.
9. 모든 커밋에서 `README.md`의 `## 작업내역`을 갱신한다.

## 작업 순서 (작업 하나마다 반복)

1. 교재에서 구현할 기능 확인
2. 관련 강의(14163·14168) 내용과 비교
3. 개선할 부분과 작업 단위 결정
4. 작은 작업 하나만 구현
5. 코드 설명 및 실제 실행·검증
6. README 기록 및 Git 커밋
7. 승인 후 다음 작업 진행

책과 강사의 방식이 다르면, 차이와 개선 이유를 먼저 설명한 뒤 적용한다.

## 프로젝트 환경

- Spring Boot 4.1.2, Java 25 (foojay 플러그인으로 자동 설치), Gradle 9.8.2 Kotlin DSL
- 기본 패키지: `com.back`
- 실행: `./gradlew bootRun` (포트 8080), 테스트: `./gradlew test`

## Git 커밋 규칙

- 제목: `기능번호-세부번호 기능명, 세부 작업` (예: `078-04 회원가입 기능, 입력값 검증 추가`)
- 커밋 하나 = 작은 작업 하나 + README 기록 하나
- 본문 형식:

  ```
  작업 내용:
  - ...

  작업 이유:
  - ...

  검증:
  - ...
  ```

- 커밋마다 같은 번호로 태그를 달고(`git tag 078-04`) 커밋과 태그를 함께 push 한다.

## README `## 작업내역` 형식

```markdown
### 078-04 회원가입 기능, 입력값 검증 추가
- [링크](https://github.com/zoek1211/jsb-plus/releases/tag/078-04)
- 작업: ...
- 이유: ...
- 결과: ...
```

## 참고 자료

- 교재: https://wikidocs.net/book/7601 (GitHub: https://github.com/pahkey/sbb3)
- 14163 강의: https://www.slog.gg/p/14163/raw (GitHub: https://github.com/jhs512/p-14163-2)
- 14168 강의: https://www.slog.gg/p/14168/raw (GitHub: https://github.com/jhs512/p-14168-2)
- 추가 기능 도전과제: https://wikidocs.net/162814

## Agent skills

### Issue tracker

Issues live in this repo's GitHub Issues (github.com/zoek1211/jsb-plus), managed with the `gh` CLI. See `docs/agents/issue-tracker.md`.

### Triage labels

Default labels: needs-triage, needs-info, ready-for-agent, ready-for-human, wontfix. See `docs/agents/triage-labels.md`.

### Domain docs

Single-context: one `GLOSSARY.md` and `docs/adr/` at the repo root. See `docs/agents/domain.md`.
