package com.exadel.discount.platform.web;

import com.exadel.discount.platform.config.JWTUtil;
import com.exadel.discount.platform.domain.Message;
import com.exadel.discount.platform.service.EmailNotificationService;
import com.exadel.discount.platform.service.UserService;
import com.exadel.discount.platform.model.dto.UserLoginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDTO userLogin) {
        try {
            userService.authenticate(userLogin);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<Message>(new Message("There isn't user with such password or email!"), HttpStatus.UNAUTHORIZED);
        }
       return new ResponseEntity<>(userService.login(userLogin), HttpStatus.OK);
    }

    @GetMapping("/validate_token")
    public ResponseEntity<Message> validateToken(@RequestParam("token") String token){
        UserDetails userDetails = null;
        try {
            userDetails = userService.loadUserByUsername(jwtUtil.extractUsername(token));
        } catch (Exception e) {
            return new ResponseEntity<>(new Message("Token is invalid."), HttpStatus.UNAUTHORIZED);
        }
        if (jwtUtil.validateToken(token, userDetails)) {
            return new ResponseEntity<>(new Message("Token is valid."), HttpStatus.OK);
        }
        return new ResponseEntity<>(new Message("Token is invalid."), HttpStatus.UNAUTHORIZED);
    }
}
