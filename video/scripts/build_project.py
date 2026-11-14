# project.tpl.html 의 {{LOG}} 자리에 실제 커밋 57개(001-01 ~ 000-06)를 넣어 project.html 을 만든다.
# Fills {{LOG}} in project.tpl.html with the real 57 commits (001-01 … 000-06) and writes project.html.
#
# 왜 스크립트로? 커밋 제목을 손으로 옮겨 적으면 틀리기 쉽다. git 에서 바로 읽어 화면과 기록이 항상 같게 한다.
# Why a script? Copying commit titles by hand invites typos; reading them from git keeps the screen true to history.
import html
import pathlib
import subprocess

here = pathlib.Path(__file__).parent
subjects = subprocess.run(
    ["git", "log", "--reverse", "--format=%s", "000-06"],
    capture_output=True, text=True, check=True, cwd=here,
).stdout.strip().split("\n")
assert len(subjects) == 57, len(subjects)

lines = []
for s in subjects:
    tag, rest = s.split(" ", 1)
    # 한 줄에 들어가도록 제목을 24자에서 자른다. / Cut the subject at 24 characters so it fits one line.
    if len(rest) > 24:
        rest = rest[:24] + "…"
    hit = ' id="pj-log-hit"' if tag == "033-01" else ""
    lines.append(f'            <div class="pj-log-line"{hit}><code>{tag}</code><span>{html.escape(rest)}</span></div>')

tpl = (here / "project.tpl.html").read_text(encoding="utf8")
out = tpl.replace("{{LOG}}", "\n".join(lines))
(here.parent / "compositions" / "project.html").write_text(out, encoding="utf8")
print("project.html:", len(lines), "log lines")
