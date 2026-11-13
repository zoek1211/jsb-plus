package com.back;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// "test" 프로필(메모리 DB)로 실행한다. 자세한 설명은 BackApplicationTests 참고.
// Runs with the "test" profile (in-memory database). See BackApplicationTests for details.
@ActiveProfiles("test")
// 스프링 앱 전체를 띄워서 테스트한다.
// Starts the whole Spring app for this test.
@SpringBootTest
// MockMvc를 준비해 준다. MockMvc는 실제 브라우저나 네트워크 없이 "가짜 HTTP 요청"을 컨트롤러에 보내는 도구다.
// 빼면 아래 mvc 필드에 넣어줄 MockMvc 객체가 없어서 테스트가 시작조차 못 한다.
// Sets up MockMvc: a tool that sends "fake HTTP requests" to controllers without a real browser or network.
// Without it, there's no MockMvc object to inject into the field below, and the test can't even start.
@AutoConfigureMockMvc
class MainControllerTest {

    // @Autowired: 스프링이 만들어 둔 객체(여기서는 MockMvc)를 이 필드에 자동으로 넣어준다 ("의존성 주입").
    // 우리가 new MockMvc()로 직접 만들지 않는다.
    // @Autowired: Spring puts an object it already created (here, MockMvc) into this field ("dependency injection").
    // We never call new MockMvc() ourselves.
    @Autowired
    private MockMvc mvc;

    // @DisplayName: 테스트 결과에 메서드 이름 대신 보여줄 설명.
    // @DisplayName: a readable label shown in test results instead of the method name.
    @Test
    @DisplayName("GET / 요청 시 SBB 를 응답한다")
    void t1() throws Exception {
        // perform(get("/")): 브라우저가 주소창에 "/" 를 입력한 것처럼 GET 요청을 보낸다.
        // perform(get("/")): sends a GET request, as if a browser typed "/" in the address bar.
        mvc.perform(get("/"))
                // andDo(print()): 요청과 응답 내용을 콘솔에 출력한다. 테스트가 실패했을 때 원인을 찾기 좋다.
                // andDo(print()): prints the request and response to the console. Handy when a test fails.
                .andDo(print())
                // andExpect: 응답이 기대와 같은지 검사한다. 다르면 테스트가 실패한다.
                // status().isOk() = HTTP 상태 코드가 200(성공)인가?
                // andExpect: checks the response matches what we expect; otherwise the test fails.
                // status().isOk() = is the HTTP status code 200 (success)?
                .andExpect(status().isOk())
                // content().string(...) = 응답 본문이 정확히 이 문자열인가?
                // content().string(...) = is the response body exactly this string?
                .andExpect(content().string("SBB"));
    }

    @Test
    @DisplayName("GET /hello 요청 시 안녕하세요 ^ ^ 를 응답한다")
    void t2() throws Exception {
        mvc.perform(get("/hello"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("안녕하세요 ^ ^"));
    }
}
