package com.registeration.demo.repositories;

import com.registeration.demo.domain.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<UserApp, Long> {
    boolean existsByUsername(String username);
    Optional<UserApp> findByUsername(String username);


    @Modifying
    @Query("UPDATE UserApp a SET a.enabled = TRUE WHERE a.username = ?1")
    int enableAppUser(String email);



}
