package com.sp.fc.web.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
<<<<<<< HEAD
import org.springframework.security.core.Authentication;
=======
import org.springframework.security.core.userdetails.UserDetails;
>>>>>>> 3-2-BasicLogin

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SecurityMessage {

<<<<<<< HEAD
    private Authentication auth;
=======
    private UserDetails user;

>>>>>>> 3-2-BasicLogin
    private String message;

}
