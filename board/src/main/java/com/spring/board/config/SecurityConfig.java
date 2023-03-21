package com.spring.board.config;

import com.spring.board.dto.UserAccountDto;
import com.spring.board.dto.security.BoardPrincipal;
import com.spring.board.repository.UserAccountRepository;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// @EnableWebSecurity // Spring Security 설정 활성화 <- 생략가능 (AutoConfig 포함)
// spring 2.0 이하 버전은 antMatchers 사용
// spring 2.0 이상 버전은 mvcMatchers 사용
@Configuration
public class SecurityConfig {

    /**
     * Spring Security 설정
     * 인증과 권한 체크
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .mvcMatchers(
                                HttpMethod.GET,
                                "/",
                                "/articles",
                                "/articles/search-hashtag"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin()
                    .and()
                .logout()
                    .logoutSuccessUrl("/")
                    .and()
                .build();
    }

    /**
     * Spring Security 에서 무시할 경로 설정 : static 리소스 경로
     * You are asking Spring Security to ignore org.springframework.boot.autoconfigure.security.servlet.StaticResourceRequest$StaticResourceRequestMatcher@52c2b9e7.
     * This is not recommended -- please use permitAll via HttpSecurity#authorizeHttpRequests instead.
     * See https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#request-matching
     */
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
//    }

    @Bean
    public UserDetailsService userDetailsService(UserAccountRepository userAccountRepository) {
        return username -> userAccountRepository
                .findById(username)
                .map(UserAccountDto::from)
                .map(BoardPrincipal::from)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다 - username : " + username));
    }

    @Bean   // Spring Security 5.0 이상에서는 PasswordEncoder 를 Bean 으로 등록해야 함
    public PasswordEncoder passwordEncoder() {  // factory 에서 위임해서 제공하는 BCryptPasswordEncoder (암호화 모듈) 사용
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
