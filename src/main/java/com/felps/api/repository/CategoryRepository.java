package com.felps.api.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.felps.api.model.Category;
import com.felps.api.model.UserAccount;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
  public Page<Category> findByUser(UserAccount user, Pageable pageable);
}
