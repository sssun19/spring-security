package com.sp.fc.teacher;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Set;

@Component
public class TeacherManager implements AuthenticationProvider, InitializingBean {

    private final HashMap<String, Teacher> teacherDB = new HashMap<>();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        if(authentication instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
            if (teacherDB.containsKey(token.getName())) {
                return getAuthenticationToken(token.getName());
            }
            return null;
        }
        TeacherAuthenticationToken token = (TeacherAuthenticationToken) authentication;

        if(teacherDB.containsKey(token.getCredentials())) {
            return getAuthenticationToken(token.getCredentials());
        }

        return null; // if 구문으로 처리할 수 없는 authentication 은 null 형태로 반환.
    }

    private TeacherAuthenticationToken getAuthenticationToken(String id) {
        Teacher teacher = teacherDB.get(id);
        return TeacherAuthenticationToken.builder()
                .principal(teacher)
                .details(teacher.getUsername())
                .authenticated(true)
                .build();
    }

    /**
     *
     * MobSecurityConfig 클래스에서 httpBasic() 을 기반으로 설정했는데
     * httpBasic 은 UsernamePasswordAuthenticationToken 을 기본으로 조회하기 때문에
     * Manager 클래스에서 커스텀으로 만든 Token 이 아닌 UsernamePasswordAuthenticationToken 도 함께 핸들링 해야 한다.
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == TeacherAuthenticationToken.class ||
                authentication == UsernamePasswordAuthenticationToken.class;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Set.of(

                new Teacher("choi","최선생", Set.of(new SimpleGrantedAuthority("ROLE_TEACHER"))),
                new Teacher("kim","김선생", Set.of(new SimpleGrantedAuthority("ROLE_TEACHER"))),
                new Teacher("hos","호선생", Set.of(new SimpleGrantedAuthority("ROLE_TEACHER")))
        ).forEach(s-> teacherDB.put(s.getId(), s));

    }
}
