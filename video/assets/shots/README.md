# 화면 캡처 / Screen captures

영상에 쓰는 실제 앱 화면. 1440x900 화면을 2배 픽셀(2880x1800)로 찍어, 영상에서 확대해도 흐리지 않다.
Real app screens for the video, captured at 1440x900 with 2x pixels (2880x1800) so push-ins stay sharp.

- 찍은 방법: 개발 DB 를 건드리지 않도록 새 H2 DB 로 앱을 8082 포트에 따로 띄우고(`TestInitData`·`DevInitData` 가 user1·user2·샘플 질문·테스트 데이터 300개를 새로 만듦), agent-browser 로 캡처.
  How: a separate app on port 8082 with a fresh H2 DB (so the dev DB is untouched), captured with agent-browser.

| 파일 | 장면 | 화면 |
|---|---|---|
| detail-1-xss.png | S01·S25 | 답변의 `<script>alert('XSS')</script>` 가 글자로만 보임 (user2) |
| list.png | S11·S14 | 질문 목록, 300개 테스트 데이터와 페이징 |
| detail-1-anon.png | S11 | 1번 질문 상세 (비로그인, 답변 입력 비활성) |
| detail-999-404.png | S12 | 없는 질문 → 404 |
| create-form.png / create-error.png | S13 | 질문 등록 폼 / 제목 비움 → "제목은 필수항목입니다." |
| detail-1-author.png | S16·S17 | 글쓴이(user1)에게 보이는 수정·삭제 버튼, 로그아웃 버튼 |
| modify-1.png | S16 | 수정 폼 |
| login-redirect.png | S18 | 비로그인으로 질문 등록 → 로그인 화면 |
| modify-403.png | S18 | user2 가 user1 의 글 수정 → 403 |
| detail-1-voted.png | S19 | 두 번 추천해도 추천 1 |
| search-sbb.png | S22 | "sbb" 검색 → 1번 질문 하나 |
| test-report.png / test-report-question-controller.png | S23 | 테스트 50개 100% 통과 / QuestionControllerTest 17개 |
| book-vs-ours.png / book-vs-ours-full.png | S24 | GitHub 의 `docs/book-vs-ours.md` 표 |

옛 버전에서만 볼 수 있는 화면(중간 테이블, LAZY 오류, 500 오류 등)은 `docs/images/` 의 당시 캡처를 쓰거나 영상 안에서 코드·테스트 줄로 그린다.
Screens that only existed in old versions use the original captures in `docs/images/` or are drawn as code/test lines.
