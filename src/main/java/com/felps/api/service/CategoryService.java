package com.felps.api.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.felps.api.model.Category;
import com.felps.api.model.UserAccount;
import com.felps.api.repository.CategoryRepository;
import com.felps.api.web.category.dto.CategoryForm;

@Service
public class CategoryService {
  @Autowired
  private CategoryRepository categoryRepository;

  public List<Category> findAll(UserAccount user) {
    return categoryRepository.findByUser(user);
  }

  public Category create(CategoryForm form, UserAccount user) {
    UserAccount loggedUser = new UserAccount();
    Category newCategory = new Category();

    loggedUser.setId(user.getId());

    BeanUtils.copyProperties(form, newCategory);
    newCategory.setUser(loggedUser);

    return categoryRepository.save(newCategory);
  }

  public Category update(CategoryForm form, UUID categoryId, UserAccount user) {
    Optional<Category> category = categoryRepository.findById(categoryId);

    if (!category.isPresent()) {
      throw new RuntimeException("Category Not Exists");
    }

    if (!category.get().getUser().getId().equals(user.getId())) {
      throw new RuntimeException("Not have permissions");
    }

    Category updatedCategory = new Category();
    UserAccount loggedUser = new UserAccount();

    loggedUser.setId(user.getId());

    BeanUtils.copyProperties(form, updatedCategory);
    updatedCategory.setId(categoryId);
    updatedCategory.setUser(loggedUser);

    return categoryRepository.save(updatedCategory);
  }

  public void delete(UUID categoryId, UserAccount user) {
    Optional<Category> category = categoryRepository.findById(categoryId);

    if (!category.isPresent()) {
      throw new RuntimeException("Category Not Exists");
    }

    if (!category.get().getUser().getId().equals(user.getId())) {
      throw new RuntimeException("Not have permissions");
    }

    categoryRepository.delete(category.get());
  }

}
