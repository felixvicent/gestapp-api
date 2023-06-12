package com.felps.api.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.felps.api.model.UserAccount;

@Repository
public interface UserRepository extends JpaRepository<UserAccount, UUID> {
  public Optional<UserAccount> findByEmail(String email);
}
