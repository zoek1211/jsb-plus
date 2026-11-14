package com.back.global.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

// 스프링 시큐리티 설정 (교재 3-05).
// 시큐리티를 추가하기만 하면 기본으로 "모든 페이지에 로그인 필요"가 되므로, 여기서 규칙을 직접 정한다.
// Spring Security configuration (book 3-05).
// Just adding Security makes "every page requires login" by default, so we define the rules ourselves here.
@Configuration
// @EnableWebSecurity: 모든 웹 요청이 시큐리티 필터를 거치도록 한다.
// @EnableWebSecurity: routes every web request through Security's filters.
@EnableWebSecurity
public class SecurityConfig {

    // SecurityFilterChain: "어떤 요청을 누구에게 허용할지" 등 보안 규칙의 묶음. @Bean 으로 등록하면 스프링 시큐리티가 사용한다.
    // SecurityFilterChain: the set of security rules, such as "which requests are allowed for whom". Registered as a @Bean, Spring Security uses it.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 지금은 모든 주소(/**)를 로그인 없이 허용한다. 로그인이 필요한 기능은 메서드마다 따로 막을 예정 (028).
                // For now, allow every URL (/**) without login. Features that need login get protected per method later (028).
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll()
                )
                // CSRF 방어: 다른 사이트가 사용자 몰래 POST 요청을 보내는 공격을 막는다.
                // 우리 폼은 th:action 덕분에 숨은 _csrf 토큰이 자동으로 들어가 통과한다 (14168 46강).
                // H2 콘솔은 그 토큰을 모르므로 /h2-console 만 예외로 둔다.
                // CSRF protection: blocks attacks where another site sends POST requests on the user's behalf.
                // Our forms pass because th:action automatically adds a hidden _csrf token (14168 lecture 46).
                // The H2 console doesn't know that token, so only /h2-console is exempt.
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                )
                // H2 콘솔 화면은 <frame> 으로 이루어져 있다. 기본 설정은 frame 을 막으므로, 같은 사이트(SAMEORIGIN) 안에서는 허용한다.
                // The H2 console is built with <frame>s. The default blocks frames, so allow them within the same site (SAMEORIGIN).
                .headers(headers -> headers
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(
                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
                )
                // 폼 로그인 (교재 3-07): 로그인 화면 주소와 로그인 성공 후 이동할 주소.
                // POST /user/login 은 우리가 만들지 않는다. 시큐리티가 username·password 를 받아 UserSecurityService 로 확인한다.
                // Form login (book 3-07): the login page URL and where to go after a successful login.
                // We don't write POST /user/login ourselves; Security receives username/password and checks them via UserSecurityService.
                .formLogin(login -> login
                        .loginPage("/user/login")
                        .defaultSuccessUrl("/")
                )
                // 로그아웃: POST /user/logout 요청이 오면 세션을 지우고 / 로 이동.
                // 교재는 GET 링크로 로그아웃하지만, 시큐리티 기본값처럼 POST + CSRF 토큰으로만 받아 다른 사이트가 몰래 로그아웃시키지 못하게 한다.
                // Logout: on POST /user/logout, clear the session and go to /.
                // The book logs out with a GET link; following Security's default, we accept only POST with a CSRF token so other sites can't log users out secretly.
                .logout(logout -> logout
                        .logoutUrl("/user/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                );
        return http.build();
    }

    // 비밀번호 암호화기를 빈으로 등록한다 (교재 3-06). UserService 가 주입받아 쓴다.
    // BCrypt: 같은 비밀번호라도 매번 다른 결과가 나오고(salt), 일부러 느리게 만들어 무작위 대입 공격을 어렵게 하는 해시 방식.
    // Registers the password encoder as a bean (book 3-06). UserService receives and uses it.
    // BCrypt: a hashing scheme that gives a different result each time even for the same password (salt), and is deliberately slow to make brute-force attacks hard.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
