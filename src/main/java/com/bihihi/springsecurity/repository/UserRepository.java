package com.bihihi.springsecurity.repository;

import com.bihihi.springsecurity.model.Role;
import com.bihihi.springsecurity.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);

    User findByRole(Role role);
}
