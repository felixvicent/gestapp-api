package com.felps.api.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.felps.api.model.Category;
import com.felps.api.model.UserAccount;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
  public Page<Category> findByUser(UserAccount user, Pageable pageable);

  List<Category> findByUser(UserAccount user);
}
