package com.sp.fc.web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "loginForm";
    }

    @ResponseBody
    @GetMapping("/auth")
    public Authentication auth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @GetMapping("/login-error")
    public String loginError(Model model){
        model.addAttribute("loginError", true);
        return "loginForm";
    }

    @PreAuthorize("hasAnyAuthority('ROLE_STUDENT')")
    @GetMapping("/StudentMain")
    public String studentPage(){
        return "StudentMain";
    }

    @PreAuthorize("hasAnyAuthority('ROLE_TEACHER')")
    @GetMapping("/TeacherMain")
    public String teacherPage(){
        return "TeacherMain";
    }


    @GetMapping("/access-denied")
    public String accessDenied(){
        return "AccessDenied";
    }
}
