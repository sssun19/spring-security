package com.sp.fc.web.config;


import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity(debug = true) //시큐리티 필터 체인의 종류를 디버그로 찍을 수 있음
@EnableGlobalMethodSecurity(prePostEnabled = true) //prePost 로 권한 체크 선언
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(User.builder()
                        .username("user2")
                        .password(passwordEncoder().encode("2222"))
                        .roles("USER"))

                .withUser(User.builder()
                        .username("admin")
                        .password(passwordEncoder().encode("3333"))
                        .roles("ADMIN"));
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.antMatcher("/**"); //모든 request 에 대해 필터 체인 동작
        http.antMatcher("/api/**"); //api 하위 request 에 대해 필터 체인 동작

        /** 두 개 이상의 필터 체인을 구성하려면 Security filter chain 을 하나 더 생성함. (예시로 SecurityConfig 클래스 추가 생성)
         * 그런 경우 어떤 필터 체인 클래스를 우선 순위로 할지 @Order(n) 어노테이션으로 필수 설정해 주어야 함.
         * 낮은 n 순서부터 먼저 체크
         */


        http.authorizeRequests((requests) -> {
            requests.antMatchers("/").permitAll() //기본 페이지는 모두 접근 가능하도록
                    .anyRequest().authenticated(); //모든 페이지를 인증 받은 후(로그인) 접근할 수 있도록
        });
        http.formLogin();
        http.httpBasic();

//        http.headers().disable()
//                .csrf().disable()
<<<<<<< HEAD
=======
//                .formLogin(login -> login.defaultSuccessUrl("/", false))
>>>>>>> 3-2-BasicLogin
//                .logout().disable()
//                .requestCache().disable();
    }
}
