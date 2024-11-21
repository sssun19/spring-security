package com.sp.fc.student;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class StudentManager implements AuthenticationProvider, InitializingBean {


    private final HashMap<String, Student> studentDB = new HashMap<>();


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        if(authentication instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
            if (studentDB.containsKey(token.getName())) {
                return getAuthenticationToken(token.getName());
            }
            return null;
        }

        StudentAuthenticationToken token = (StudentAuthenticationToken) authentication;

        if(studentDB.containsKey(token.getCredentials())) {
            return getAuthenticationToken(token.getCredentials());
        }

        return null; // if 구문으로 처리할 수 없는 authentication 은 null 형태로 반환.

    }

    private StudentAuthenticationToken getAuthenticationToken(String id) {
        Student student = studentDB.get(id);
        return StudentAuthenticationToken.builder()
                .principal(student)
                .details(student.getUsername())
                .authenticated(true)
                .authorities(student.getRole())
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
        return authentication == StudentAuthenticationToken.class ||
                authentication == UsernamePasswordAuthenticationToken.class;
    }

    public List<Student> myStudents(String teacherId) {
        return studentDB.values().stream().filter(s-> s.getTeacherId().equals(teacherId))
                .collect(Collectors.toList());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Set.of(

                new Student("hong","홍길동", Set.of(new SimpleGrantedAuthority("ROLE_STUDENT")), "choi"),
                new Student("kang","강아지", Set.of(new SimpleGrantedAuthority("ROLE_STUDENT")),"choi"),
                new Student("rang","호랑이", Set.of(new SimpleGrantedAuthority("ROLE_STUDENT")),"choi")
        ).forEach(s-> studentDB.put(s.getId(), s));

    }
}
