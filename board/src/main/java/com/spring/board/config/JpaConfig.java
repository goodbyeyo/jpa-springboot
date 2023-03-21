package com.spring.board.config;

import com.spring.board.dto.security.BoardPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@EnableJpaAuditing  // JPA Auditing 활성화 (생성일시, 수정일시, 생성자, 수정자 자동화)
@Configuration
public class JpaConfig {

    /**
     * SecurityContextHolder : SpringSecurity 에서 사용하는 Security Context 저장하는 곳
     * SecurityContext : Authentication 객체를 저장하는 곳
     * Authentication : 인증 정보를 저장하는 곳
     * Authentication.getPrincipal : 인증된 사용자 정보를 리턴 (UserDetails 타입)
     * UserDetails : 인증된 사용자 정보를 담고 있는 인터페이스
     */
    @Bean   // 리턴되는 객체가 IOC 컨테이너 안에 빈으로 등록
    public AuditorAware<String> auditorAware() {
        return () -> Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(BoardPrincipal.class::cast)
                .map(BoardPrincipal::getUsername);
    }
}
