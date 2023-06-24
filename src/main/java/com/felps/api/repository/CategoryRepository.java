package com.felps.api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.felps.api.model.Category;
import com.felps.api.model.UserAccount;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
  public List<Category> findByUser(UserAccount user);
}
