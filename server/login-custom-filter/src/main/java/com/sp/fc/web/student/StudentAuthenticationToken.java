package com.sp.fc.web.student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentAuthenticationToken implements Authentication {

    private Student principal; // 인증된 사용자의 주요 정보 (Student 객체 참조)
    private String credentials; // 인증 과정에서 사용되는 자격 증명. 비밀번호 등.
    private String details; // 인증 요청에 대한 부가 정보. IP 주소 등 세부 정보.
    private boolean authenticated; // 인증 여부
    private Set<GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return principal == null ? new HashSet<>() : principal.getRole();
    }

    @Override
    public String getName() {
        return principal == null ? "" : principal.getUsername();
    }
}
