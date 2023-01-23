package com.spring.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing  // JPA Auditing 활성화 (생성일시, 수정일시, 생성자, 수정자 자동화)
@Configuration
public class JpaConfig {

    @Bean   // 리턴되는 객체가 IOC 컨테이너 안에 빈으로 등록
    public AuditorAware<String> auditorAware() {
        // TODO : 로그인 기능 및 스프링 시큐리티 인증 구현 후 수정
        return () -> Optional.of("wook");
    }
}
