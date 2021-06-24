package com.exadel.discount.platform.repository;

import com.exadel.discount.platform.domain.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, UUID> {

    UserDetails findUserDetailsByUserId(UUID userId);
}
