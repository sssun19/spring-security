package com.sp.fc.web.config;

import com.sp.fc.user.service.SpUserService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.*;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionEvent;
import javax.sql.DataSource;
import java.time.LocalDateTime;

@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true) // 설정된 role 확인해 접근 제한
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * SecurityContextPersistenceFilter filter;
     * RememberMeAuthenticationFilter;
     * TokenBasedRememberMeServices tokenBasedRememberMeServices;
     * PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices;
     */

    private final SpUserService spUserService;
    private final DataSource dataSource;

    public SecurityConfig(SpUserService userService, DataSource dataSource) {
        this.spUserService = userService;
        this.dataSource = dataSource;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(spUserService);
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

    /**
     * 원래 web.xml 이 있다면 Servlet Listener 를 등록해야 하는데 springboot 에서는 bean 으로 대체
     * session 이 생성 되고 만료 되는 사이클을 알아보기 위해 리스너 설정
     */
    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher() {
            @Override
            public void sessionCreated(HttpSessionEvent event) {
                super.sessionCreated(event);
                System.out.printf("===>>  [%s] 세션 생성됨 %s  \n", LocalDateTime.now(), event.getSession().getId());
            }

            @Override
            public void sessionDestroyed(HttpSessionEvent event) {
                super.sessionDestroyed(event);
                System.out.printf("===>>  [%s] 세션 만료됨 %s  \n", LocalDateTime.now(), event.getSession().getId());

            }

            @Override
            public void sessionIdChanged(HttpSessionEvent event, String oldSessionId) {
                super.sessionIdChanged(event, oldSessionId);
                System.out.printf("===>>  [%s] 세션 아이디 변경 %s:%s  \n", LocalDateTime.now(), oldSessionId, event.getSession().getId());

            }
        });
    }

    @Bean
    SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }


    @Bean
    PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
        repository.setDataSource(dataSource);
        try {
            repository.removeUserTokens("1");
        } catch (Exception ex) {
            repository.setCreateTableOnStartup(true);
        }
        return repository;
    }

    @Bean
    PersistentTokenBasedRememberMeServices rememberMeServices() {
        PersistentTokenBasedRememberMeServices service =
                new PersistentTokenBasedRememberMeServices(
                        "hello",
                        spUserService,
                        tokenRepository()) {
                    @Override
                    protected Authentication createSuccessfulAuthentication(HttpServletRequest request, UserDetails user) {
                        return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), null);
//                        return super.createSuccessfulAuthentication(request, user);
                    }
                };
        service.setAlwaysRemember(true);
        return service;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests(request -> {
                    request.antMatchers("/", "/error").permitAll()
                            .antMatchers("/admin/**").hasRole("ADMIN")
                            .anyRequest().authenticated();
                    /*
                      이렇게 루트 페이지만 접근 허용하고 다른 request 접근을 막아버리면
                      페이지들에 대해 보안을 걸었기 때문에 CSS 가 제대로 내려가지 않는 경우가 발생한다.
                      웹 리소스(css,...)에 대해 security filter 를 적용되지 않도록 ignore 시켜야 함.
                     */

                })
                .formLogin(login -> login.loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/", false)
                        .failureUrl("/login-error")

                )
                .logout(logout -> logout.logoutSuccessUrl("/"))
                .exceptionHandling(error -> error
//                        .accessDeniedPage("/access-denied")
                        .accessDeniedHandler(new CustomDeniedHandler())

                )
                .rememberMe(r -> r
                                .rememberMeServices(rememberMeServices())
                        /*
                        .alwaysRemember(true) rememberMeService 를 커스텀 했기 때문에 여기서 alwaysRemember true 값을 줘도 설정 안 됨.
                        rememberMeServices 에서 직접 설정해주어야 함.
                         */
                )
                .sessionManagement(s -> s
//                        .sessionCreationPolicy(p-> SessionCreationPolicy.STATELESS)
                                .sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::changeSessionId)
                                .maximumSessions(2) // session 최대 1개 관리
                                .maxSessionsPreventsLogin(false) // 새로 들어온 session 은 인정, 기존 session 은 만료
                                .expiredUrl("/session-expired") // session 이 만료 되면 /session-expired 페이지로 redirect
                )
        ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/sessions", "/session/expire", "/session-expired")
                .requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations(), // image, css, js bootstrap
                        PathRequest.toH2Console()) // 외부 H2 DB를 사용하려면 리소스 경로 오픈했던 것처럼 열어주어야 한다.
        ;

    }


}
