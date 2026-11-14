package com.back.global.util;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Component;

// 여러 곳에서 쓰는 도구 모음 (교재 3-12).
// @Component: 스프링이 객체(빈)로 만들어 둔다.
// 템플릿에서는 GlobalModelAttributes 가 model 에 넣어 준 ${commonUtil.markdown(...)} 으로 쓴다
// (교재의 ${@commonUtil...} 빈 직접 호출은 타임리프 3.1.5 에서 막혀 있다).
// Shared helpers (book 3-12).
// @Component: Spring creates it as an object (bean).
// Templates use it as ${commonUtil.markdown(...)}, which GlobalModelAttributes puts into the model
// (the book's direct bean call ${@commonUtil...} is blocked in Thymeleaf 3.1.5).
@Component
public class CommonUtil {
    // 마크다운 글자를 읽어 구조(Node)로 바꾸는 파서. 한 번 만들어 계속 쓴다.
    // The parser that reads Markdown text into a structure (Node). Built once and reused.
    private final Parser parser = Parser.builder().build();

    // 구조(Node)를 HTML 문자열로 바꾸는 렌더러.
    // escapeHtml(true): 글 안에 직접 쓴 HTML 태그(<script> 등)는 실행되지 않도록 글자(&lt;script&gt;)로 바꾼다.
    // 교재는 이 옵션이 없어서 누군가 글에 <script> 를 넣으면 다른 사람의 브라우저에서 실행될 수 있다(XSS 공격).
    // The renderer that turns the structure (Node) into an HTML string.
    // escapeHtml(true): HTML tags typed directly into a post (<script>, ...) become plain text (&lt;script&gt;) so they never run.
    // The book doesn't set this, so someone could put <script> in a post and have it run in other people's browsers (an XSS attack).
    private final HtmlRenderer renderer = HtmlRenderer.builder()
            .escapeHtml(true)
            .build();

    // 마크다운 → HTML. 예) "**굵게**" → "<p><strong>굵게</strong></p>"
    // Markdown → HTML. e.g. "**bold**" → "<p><strong>bold</strong></p>"
    public String markdown(String markdown) {
        Node document = parser.parse(markdown);
        return renderer.render(document);
    }
}
