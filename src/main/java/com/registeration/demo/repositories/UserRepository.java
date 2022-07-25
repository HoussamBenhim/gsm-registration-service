package com.registeration.demo.repositories;

import com.registeration.demo.domain.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserApp, Long> {
    boolean existsByUsername(String username);
    Optional<UserApp> findByUsername(String username);
}
