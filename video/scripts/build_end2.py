# end2.tpl.html 을 채워 compositions/end2.html 을 만든다.
# Fills end2.tpl.html and writes compositions/end2.html.
#   {{TESTS}}       테스트 클래스 8개와 테스트 수 (2026-11-14 테스트 리포트 기준, 합계 50)
#                   the 8 test classes and their test counts (from the 2026-11-14 test report, total 50)
#   {{ROWS}}        docs/book-vs-ours.md 의 실제 34줄 (주제 + 첫 커밋 태그)
#                   the real 34 rows of docs/book-vs-ours.md (topic + first commit tag)
#   {{STOPS}}, {{STOPS_GREEN}}  훅과 같은 정거장 6개 / the same 6 stops as the hook
#
# 왜 스크립트로? 표를 손으로 옮기면 틀리기 쉽다. 문서에서 바로 읽어 화면과 기록이 늘 같게 한다.
# Why a script? Copying the table by hand invites mistakes; reading the doc keeps screen and record in sync.
import html
import pathlib
import re

here = pathlib.Path(__file__).parent
repo = here.parent.parent

TESTS = [
    ("AnswerControllerTest", 7),
    ("BackApplicationTests", 1),
    ("CommonUtilTest", 2),
    ("MainControllerTest", 1),
    ("QuestionControllerTest", 17),
    ("QuestionRepositoryTest", 12),
    ("QuestionServiceTest", 5),
    ("UserControllerTest", 5),
]
assert sum(n for _, n in TESTS) == 50

tests = "\n".join(
    f'        <div class="e2-cls"><span class="n">{name}</span><span class="c">{n}</span>'
    + "".join('<span class="e2-cell"></span>' for _ in range(n))
    + "</div>"
    for name, n in TESTS
)

rows = []
for line in (repo / "docs" / "book-vs-ours.md").read_text(encoding="utf8").splitlines():
    if not line.startswith("| ") or line.startswith("| 주제"):
        continue
    cells = [c.strip() for c in line.strip().strip("|").split("|")]
    topic = cells[0].replace("`", "")
    tag = re.findall(r"\[(\d{3}-\d{2})\]", cells[-1])[0]
    rows.append(f'            <div class="e2-row"><span>{html.escape(topic)}</span><code>{tag}</code></div>')
assert len(rows) == 34, len(rows)

names = ["저장", "목록", "답변", "보안", "추천", "검색"]
stops = "\n".join(
    f'        <div class="e2-stop" style="left: {i * 20}%"><i><b></b></i>{n}</div>' for i, n in enumerate(names)
)
stops_green = "\n".join(
    f'        <div class="e2-stop" style="left: {i * 20}%"><i style="background: var(--ours)"></i>{n}</div>'
    for i, n in enumerate(names)
)

out = (here / "end2.tpl.html").read_text(encoding="utf8")
for key, val in {"{{TESTS}}": tests, "{{ROWS}}": "\n".join(rows), "{{STOPS}}": stops, "{{STOPS_GREEN}}": stops_green}.items():
    out = out.replace(key, val)
(here.parent / "compositions" / "end2.html").write_text(out, encoding="utf8")
print("end2.html:", len(TESTS), "test classes,", len(rows), "rows")
