package com.sp.fc.web.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;

@EnableWebSecurity(debug = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(
                        User.withDefaultPasswordEncoder()
                                .username("user1")
                                .password("1111")
                                .roles("USER")
                ).withUser(
                        User.withDefaultPasswordEncoder()
                                .username("admin")
                                .password("2222")
                                .roles("ADMIN")
                );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(request->{
                    request.antMatchers("/").permitAll()
                            .anyRequest().authenticated();
                    /**
                     * 이렇게 루트 페이지만 접근 허용하고 다른 request 접근을 막아버리면
                     * 페이지들에 대해 보안을 걸었기 때문에 CSS 가 제대로 내려가지 않는 경우가 발생한다.
                     * 웹 리소스(css,...)에 대해 security filter 를 적용되지 않도록 ignore 시켜야 함.
                     */
                })
                .formLogin(login->login.loginPage("/login").permitAll()
                        .defaultSuccessUrl("/", false)
                        .failureUrl("/login-error")
                )
                .logout(logout -> logout.logoutSuccessUrl("/"))
        ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations()); // image, css, js bootstrap
    }
}
