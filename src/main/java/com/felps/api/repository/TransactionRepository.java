package com.felps.api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.felps.api.model.Transaction;
import com.felps.api.model.UserAccount;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
  public List<Transaction> findByUser(UserAccount user);
}
