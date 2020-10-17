package com.obidex.webserver.auth;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface ApplicationUserRepository extends MongoRepository<ApplicationUser, String> {
    Optional<UserDetails> findByUsername(@Param("username") String username);
}
