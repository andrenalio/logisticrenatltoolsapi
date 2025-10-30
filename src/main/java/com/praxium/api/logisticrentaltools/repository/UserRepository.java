package com.praxium.api.logisticrentaltools.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.praxium.api.logisticrentaltools.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
}
