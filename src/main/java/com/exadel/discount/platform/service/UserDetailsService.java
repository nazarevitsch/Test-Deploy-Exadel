package com.exadel.discount.platform.service;

import com.exadel.discount.platform.domain.UserDetails;
import com.exadel.discount.platform.exception.NotFoundException;
import com.exadel.discount.platform.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserDetailsService {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    public UserDetails findUserDetailsByUserId(UUID userId) {
        Optional<UserDetails> userDetails = Optional.of(userDetailsRepository.findUserDetailsByUserId(userId));
        return userDetails.orElseThrow(() -> new NotFoundException("User details with id " + userId +
                " does not exist.", userId, UserDetails.class));
    }
}
