package com.sp.fc.web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
<<<<<<< HEAD
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
=======
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
>>>>>>> 3-2-BasicLogin
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

<<<<<<< HEAD
    @RequestMapping("/")
    public String index() {
        return "홈페이지";
    }

    @RequestMapping("/auth")
    public Authentication auth() {
        return SecurityContextHolder.getContext()
                .getAuthentication();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @RequestMapping("/user")
    public SecurityMessage user() {

        return SecurityMessage.builder()
                .auth(SecurityContextHolder.getContext().getAuthentication())
                .message("User 정보")
=======
    @GetMapping("/")
    public SecurityMessage hello(@AuthenticationPrincipal UserDetails user){
        return SecurityMessage.builder()
                .user(user)
                .message("hello")
                .build();
    }


    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @GetMapping("/user")
    public SecurityMessage user(@AuthenticationPrincipal UserDetails user){
        return SecurityMessage.builder()
                .user(user)
                .message("user page")
>>>>>>> 3-2-BasicLogin
                .build();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
<<<<<<< HEAD
    @RequestMapping("/admin")
    public SecurityMessage admin() {

        return SecurityMessage.builder()
                .auth(SecurityContextHolder.getContext().getAuthentication())
                .message("관리자 정보")
=======
    @GetMapping("/admin")
    public SecurityMessage admin(@AuthenticationPrincipal UserDetails user){
        return SecurityMessage.builder()
                .user(user)
                .message("admin page")
>>>>>>> 3-2-BasicLogin
                .build();
    }

}
