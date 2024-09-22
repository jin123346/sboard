package com.sboard.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    /*
         스프링 시큐리티
         - Spring 프레임워크에서 제공하는 보안관련 처리를 위한 프레임워크
         - Spring 기반 프로젝트는  Spring Security로 보안(인증,인가) 처리 권장

         인증설정
         - 로그인, 로그아웃 페이지 및 요청주소 설정
         - 기존 인증(로그인, 로그아웃)을 .security가 제공하는 기본인증페이지 동작
         -  REST API 기반 인증에서는 token방식을 이용하기 때문에 로그인, 로그아웃 비활성

         인가설정
         - MyUserDetails의 사용자 권한 목록을 제공하는 getAuthorities()에서 반드시 접두어로 ROLE_. 붙여야 됨
         - 'ROLE_' 접두어를 안붙이면 hasAuthority(). hasAnyAuthority()로 권한설정해야함
         - 존재하지 않은 요청주소에 대해서 시큐리티는 로그인 페이지로 기본 redirect 수행하기 때문에 마지막에 anyRequest().permitAll() 권한 설정
         - anyRequest.permitAll()을 꼭 마지막에 넣어줘야한다.

     */


    //어플리케이션 실행시 등록됨
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 로그인 설정
        http.formLogin(login -> login
                .loginPage("/user/login")
                .defaultSuccessUrl("/")
                .failureUrl("/user/login?success=100")
                .usernameParameter("uid")
                .passwordParameter("pass"));

        // 로그아웃 설정
        http.logout(logout -> logout
                .invalidateHttpSession(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                .logoutSuccessUrl("/user/login?success=101"));
        // 인가 설정
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers("/staff/**").hasAnyRole("ADMIN", "MANAGER", "STAFF")
                .anyRequest().permitAll());

        // 기타 보안 설정
        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        //평문을 암호화시킬때 암호문 만들때 도와주는 encoder
        return new BCryptPasswordEncoder();
    }
}
