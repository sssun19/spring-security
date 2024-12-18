package com.sp.fc.web.config;

import com.sp.fc.web.service.Paper;
import lombok.Getter;
import lombok.Setter;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

import java.lang.reflect.Method;

@Getter
@Setter
public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot
        implements MethodSecurityExpressionOperations {

    MethodInvocation invocation;
    public CustomMethodSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
        super(authentication);
        this.invocation = invocation;
    }

    private Object filterObject;
    private Object returnObject;

    public boolean isStudent() {
        return getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT"));

        /*
        .filter(a -> a.getAuthority().equals("ROLE_STUDENT"))
                .findAny().isPresent(); 를 .anyMatch() 메서드로
         */
    }

    public boolean isTutor() {
        return getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_TUTOR"));

    }

    public boolean notPrepareState(Paper paper) {
        return paper.getState() != Paper.State.PREPARE;
    }


    @Override
    public Object getThis() {
        return this;
    }
}
