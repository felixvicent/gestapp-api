package com.felps.api.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.felps.api.model.BankAccount;
import com.felps.api.model.UserAccount;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, UUID> {
  List<BankAccount> findAllByUser(UserAccount user);

  Optional<BankAccount> findByIdAndUser(UUID bankAccountId, UserAccount user);

}
