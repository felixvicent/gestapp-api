package com.felps.api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.felps.api.model.Transaction;
import com.felps.api.model.UserAccount;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
  public Page<Transaction> findByUser(UserAccount user, Pageable pageable);

  @Query(value = "SELECT * FROM transactions WHERE type = ?1 AND user_id = ?2 AND date_part('year', datetime) = ?3", nativeQuery = true)
  List<Transaction> findByTypeUserAndDate(String type, UUID userId, int year);
}
