package com.sp.fc.web.config;

import com.sp.fc.web.student.StudentManager;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity(debug = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /** StudentManager (authentication provider) 설정하기
     *  1. SecurityConfig 에서 provider 로 설정할 클래스 등록. constructor 로 주입 받기.
     *  2. AuthenticationManagerBuilder 로 Provider 클래스 설정.
     */
    private final StudentManager studentManager;

    public SecurityConfig(StudentManager studentManager) {
        this.studentManager = studentManager;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(studentManager);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(request->
                        request.anyRequest().permitAll()
                ).formLogin(
                        login-> login.loginPage("/login")
        )
                ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                ;
    }
}
