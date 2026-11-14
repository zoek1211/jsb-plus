package com.back.global.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

// CommonUtil.markdown 테스트. 스프링 없이 객체를 직접 만들어 검사하는 "단위 테스트"라 빠르다.
// Tests for CommonUtil.markdown. A "unit test" that creates the object directly without Spring, so it's fast.
class CommonUtilTest {
    private final CommonUtil commonUtil = new CommonUtil();

    @Test
    @DisplayName("**굵게** 는 <strong> 으로, - 목록은 <ul><li> 로 바뀐다")
    void t1() {
        String html = commonUtil.markdown("**굵게**\n\n- 하나\n- 둘");

        assertThat(html).contains("<strong>굵게</strong>");
        assertThat(html).contains("<li>하나</li>");
    }

    @Test
    @DisplayName("글에 직접 쓴 <script> 는 실행되지 않도록 글자로 바뀐다 (XSS 방지)")
    void t2() {
        String html = commonUtil.markdown("<script>alert('xss')</script>");

        // 실제 태그로 남으면 안 되고, &lt;script&gt; 처럼 글자로 바뀌어야 한다
        // It must not remain a real tag; it must become text like &lt;script&gt;
        assertThat(html).doesNotContain("<script>");
        assertThat(html).contains("&lt;script&gt;");
    }
}
