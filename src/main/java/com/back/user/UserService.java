package com.back.user;

import com.back.global.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 회원 관련 처리 규칙 (교재 3-06).
// The rules for members (book 3-06).
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    // 비밀번호 암호화기. SecurityConfig 에 @Bean 으로 등록한 BCryptPasswordEncoder 가 주입된다.
    // The password encoder. The BCryptPasswordEncoder registered as a @Bean in SecurityConfig is injected.
    private final PasswordEncoder passwordEncoder;

    // 회원 가입. 비밀번호는 반드시 암호화해서 저장한다.
    // 같은 아이디·이메일이 이미 있으면 DB 유일 제약 때문에 DataIntegrityViolationException 이 던져진다 (컨트롤러에서 처리).
    // Sign up. The password is always stored encrypted.
    // If the username or email already exists, the database's unique constraint throws DataIntegrityViolationException (handled in the controller).
    @Transactional
    public SiteUser create(String username, String email, String password) {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setEmail(email);
        // encode: "1234" → "$2a$10$..." 처럼 되돌릴 수 없는 문자열로 바꾼다. DB가 털려도 원래 비밀번호를 알 수 없다.
        // encode: turns "1234" into an irreversible string like "$2a$10$...". Even if the database leaks, the original password stays unknown.
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    // 아이디로 회원 찾기 (028-01). 로그인한 사용자(Principal)의 이름으로 SiteUser 를 얻을 때 쓴다.
    // Find a member by username (028-01). Used to get the SiteUser for the logged-in user's name (Principal).
    public SiteUser getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("siteuser not found"));
    }
}
