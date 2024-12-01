package com.sp.fc.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SessionController {

    @Autowired
    private SessionRegistry sessionRegistry;

    @GetMapping("/sessions")
    public String sessions() {
        return "sessionList";
    }

    @GetMapping("/session/expire")
    public String expireSession(String sessionId) {
        return "redirect:/sessions";
    }

    public String sessionExpired() {
        return "/sessionExpired";
    }
}
