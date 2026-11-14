package com.back.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// 회원 리포지터리. findByUsername 은 로그인(027-01)에서 아이디로 회원을 찾을 때 쓴다.
// The member repository. findByUsername is used at login (027-01) to look up a member by username.
public interface UserRepository extends JpaRepository<SiteUser, Integer> {
    // → SELECT ... FROM site_user WHERE username = ?
    Optional<SiteUser> findByUsername(String username);
}
