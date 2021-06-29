package com.exadel.discount.platform.web;

import com.exadel.discount.platform.domain.Message;
import com.exadel.discount.platform.domain.UserDetails;
import com.exadel.discount.platform.exception.NotFoundException;
import com.exadel.discount.platform.service.UserDetailsService;
import com.exadel.discount.platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/user_details")
public class UserDetailsController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @GetMapping("/{user_id}")
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'USER')")
    public ResponseEntity<?> getUserDetailsByUserId(@PathVariable("user_id")UUID userId, Principal principal) {
        if (userService.findUserByEmail(principal.getName()).getId().equals(userId)) {
            return new ResponseEntity<>(userDetailsService.findUserDetailsByUserId(userId), HttpStatus.OK);
        }
        return new ResponseEntity<>(new Message("You are not you!"), HttpStatus.UNAUTHORIZED);
    }
}
