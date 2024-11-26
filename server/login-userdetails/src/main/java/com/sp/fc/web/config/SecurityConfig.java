package com.sp.fc.web.config;

import com.sp.fc.user.service.SpUserService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;

@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true) // 설정된 role 확인해 접근 제한
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SpUserService userService;

    public SecurityConfig(SpUserService userService) {
        this.userService = userService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    /**
     * RoleHierarchy 로 ADMIN 권한을 가진 계정은 USER 권한을 모두 통과할 수 있도록 Hierarchy 설정
     */
    @Bean
    RoleHierarchy roleHierarchy() {
                RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
                roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
                return roleHierarchy;

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
                .exceptionHandling(exception -> exception.accessDeniedPage("/access-denied"))
        ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations()); // image, css, js bootstrap

    }


}
