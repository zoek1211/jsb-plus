# REVIEW — "질문 하나의 여행" (jsb-plus 결과 영상)

리뷰 날짜 2026-11-14 · 길이 308.56 s (5분 9초) · 1920x1080 30fps · 장면 25개 / 장면 파일 12개
Reviewed 2026-11-14 · 308.56 s (5:09) · 1920x1080 30 fps · 25 scenes in 12 scene files

## 만든 방향 / Creative calls
- 줄거리 A "질문 하나의 여행"을 골랐다: 샘플 질문 "sbb가 무엇인가요?" 카드가 훅 → 저장(DB) → 목록 → 검색 → 마지막 화면까지 따라다닌다. 만들지 않은 줄거리: B 커밋 타임라인 비행, C 통하지 않은 공격들 (`STORYLINES.md`).
  Picked storyline A; the question card is the carrier from hook to closing frame. Not built: B (commit-timeline flight), C (failed attacks).
- 룩은 코드 리뷰 diff: 밝은 #F6F8FA 바탕, 빨강 = 교재·실패, 초록 = 우리·통과. 실제 앱 화면은 2880x1800 으로 다시 캡처.
- 내레이션 Edge TTS ko-KR-SunHiNeural 25줄이 시계 — 장면 시각은 문장 시각(.srt)에 맞춤.
- 처음과 끝이 같은 구성(한 줄 + 질문 카드 + 정거장 6개): 처음엔 회색 길, 끝엔 초록 길.
- 음악 없음: HeyGen 로그인이 없어 배경음을 고를 수 없었다. 효과음 22개(Pixabay, 번들)만 넣음.
  No music: no HeyGen sign-in for the catalog. 22 bundled Pixabay effects only.

## 측정한 것 / Measured
- `npx hyperframes check`: 오류 0, 경고는 아래 "일부러 남긴 경고"뿐.
- 내레이션 25개 모두 -16 LUFS 로 맞춤(`normalize-audio`). 효과음은 측정한 최고점 시각으로 그림보다 1프레임 늦게 놓음, 음량 0.25–0.35 (50 에서 한 번만 0.5).
- `snapshot --review`: 장면별 프레임 22장, 장 사이 이음새 10곳(−0.2 ~ +0.2 s), 휴대폰 폭 시트.
- 렌더 (`renders/jsb-plus-video.mp4`, quality delivery): 5분 8.6초, 1920x1080 H.264 30fps + AAC, 19.9 MB, 렌더 3분 45초.
  `hyperframes audio` 측정: 전체 -13 LUFS, 순간 최대 -11.1 LUFS, true peak -1 dBTP, 클리핑 0. 목표(-16 ~ -14)보다 1 LU 정도 크다 — 효과음이 더해진 만큼. 필요하면 효과음 음량을 낮춘다.
  Render measured: -13 LUFS integrated, -1 dBTP true peak, no clipping — about 1 LU above the -16 to -14 target because the effects add up.

## 사람이 확인하지 않은 것 / Not checked by a person
- **소리는 측정만 했고 아무도 듣지 않았다.** 특히 영어 용어를 한글로 적은 줄(S04 "스프링부트 사 점 일 점 이", S15 "씨에스알에프", S20 "타임리프 삼 점 일 점 오")의 발음.
  The sound was measured, never heard — especially the Hangul-spelled English terms.
- 휴대폰 크기(360 px)에서 코드 글자는 읽을 수 없다. 이 영상은 발표 화면·노트북으로 보는 것을 전제로 했다.

## 리뷰 노트 / Notes (t · 어디 · 문제 → 바꾼 것)
- **P1 (고침)** t=92.1, 108.9, 160.2, 180.1, 207.7, 240.3, 279.56 · 장 사이 이음새 · 다음 장면의 첫 카드가 투명에서 시작해 0.2–0.4 s 빈 화면이 깜빡임 → 첫 요소를 opacity 0.4 + blur 12px 에서 0 초에 시작 · 나머지 등장 순서는 그대로.
- **P1 (고침, 앞 단계)** 웹 장 목록·폼 캡처 글자가 14px 정도 → 카메라 확대 / 캡처 잘라 확대; 보안 장 로그인·403 캡처 더 좁게 자름.
- **P2 (남김)** 여러 장면에서 카드가 화면의 절반 정도만 차지하고 오른쪽·아래가 비어 있다 (예: 92 s 가짜 초록불, 109 s LAZY). 히어로가 화면의 40% 이상을 채우라는 기준에 못 미치는 장면이 있다.
- **P2 (남김)** 장 사이 전환이 대부분 "흐림 교체" 한 종류. 카드 운반(훅→과제, 과제→DB, DB→웹, 검색→카드)은 이어지지만, 보안·마지막 장 앞은 흐림 교체뿐.

## 가장 약한 장면과, 시간이 더 있다면 / Weakest shot
- 가장 약한 곳: S17 (207.7–218 s) 비교 표 — 표 하나와 한 줄 문장뿐이라 "슬라이드"처럼 보인다.
  시간이 더 있다면: 표 대신 실제 내비게이션 바의 로그아웃 버튼과 추천 버튼에 카메라가 다가가 GET 링크 → 폼으로 바뀌는 장면으로.
- 장면 크기: 카드가 작은 장면들(DB·보안)을 1.2–1.4 배 키워 화면을 더 채우기.
- 배경음: HeyGen 로그인 후 "calm minimal pulse, light piano, 90 bpm" 을 0.12 볼륨으로.

## 일부러 남긴 경고 / Warnings kept on purpose
- `line_unread` — 과제 장 git log 줄들(빠르게 흐름): 줄을 읽게 하는 장면이 아니라 커밋 57개라는 "양"을 보여주는 장면.
- `line_unread` — 과제 장 첫 0.35 s 의 한 줄·정거장·카드 본문: 훅 마지막 화면의 복제라 실제로는 6초 넘게 화면에 있던 글자.
- `composition_file_too_large` — `project.html` 에 실제 커밋 57줄이 들어 있어서 (스크립트로 생성).
