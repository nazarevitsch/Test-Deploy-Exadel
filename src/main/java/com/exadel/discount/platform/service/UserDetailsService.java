package com.exadel.discount.platform.service;

import com.exadel.discount.platform.domain.UserDetails;
import com.exadel.discount.platform.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserDetailsService {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    public UserDetails findUserDetailsByUserId(UUID userId) {
        return userDetailsRepository.findUserDetailsByUserId(userId);
    }
}
