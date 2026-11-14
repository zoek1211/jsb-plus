package com.back.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

// 로그인할 때 스프링 시큐리티가 "이 아이디의 회원 정보를 달라"고 부르는 서비스 (교재 3-07).
// UserDetailsService 를 구현해 빈으로 등록하면, 시큐리티의 폼 로그인이 자동으로 이것을 사용한다.
// The service Spring Security calls at login to ask "give me the member with this username" (book 3-07).
// Implementing UserDetailsService and registering it as a bean makes Security's form login use it automatically.
@Service
@RequiredArgsConstructor
public class UserSecurityService implements UserDetailsService {
    private final UserRepository userRepository;

    // 로그인 폼에 입력한 아이디로 회원을 찾아, 시큐리티가 이해하는 UserDetails 로 바꿔 돌려준다.
    // 비밀번호 비교는 시큐리티가 PasswordEncoder 로 직접 한다 (여기서는 암호화된 비밀번호만 넘긴다).
    // Finds the member by the username typed into the login form and returns it as UserDetails, which Security understands.
    // Security itself compares the password using the PasswordEncoder (we only hand over the encrypted password).
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SiteUser siteUser = userRepository.findByUsername(username)
                // 없는 아이디면 이 예외 → 시큐리티가 로그인 실패로 처리한다.
                // An unknown username throws this → Security treats it as a failed login.
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        // 아이디가 admin 이면 관리자, 나머지는 일반 회원 권한
        // The username "admin" gets the admin role; everyone else gets the user role
        UserRole role = "admin".equals(username) ? UserRole.ADMIN : UserRole.USER;
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role.getValue()));

        // User: 시큐리티가 제공하는 UserDetails 구현체 (아이디, 암호화된 비밀번호, 권한)
        // User: Security's own UserDetails implementation (username, encrypted password, authorities)
        return new User(siteUser.getUsername(), siteUser.getPassword(), authorities);
    }
}
