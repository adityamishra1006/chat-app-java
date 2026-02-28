package com.example.auth.repo;

import com.example.auth.entity.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserCredentialsRepo extends JpaRepository<UserCredentials, UUID> {
    Optional<UserCredentials> findByUsername(String username);
    Optional<UserCredentials> findByEmail(String email);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
