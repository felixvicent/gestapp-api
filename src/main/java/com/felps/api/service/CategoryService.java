package com.felps.api.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.felps.api.exceptions.NoHasPermissionException;
import com.felps.api.exceptions.ResourceNotFoundException;
import com.felps.api.model.Category;
import com.felps.api.model.UserAccount;
import com.felps.api.repository.CategoryRepository;
import com.felps.api.web.category.dto.CategoryDTO;
import com.felps.api.web.category.dto.CategoryForm;

@Service
public class CategoryService {
  @Autowired
  private CategoryRepository categoryRepository;

  public Page<CategoryDTO> findAll(UserAccount user, Pageable pageable) {
    return categoryRepository.findByUser(user, pageable).map(category -> entityToDTO(category));
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
      throw new ResourceNotFoundException("Categoria não existe");
    }

    if (!category.get().getUser().getId().equals(user.getId())) {
      throw new NoHasPermissionException("Você não tem permissão para essa ação");
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
      throw new ResourceNotFoundException("Categoria não existe");
    }

    if (!category.get().getUser().getId().equals(user.getId())) {
      throw new NoHasPermissionException("Você não tem permissão parea essa ação");
    }

    categoryRepository.delete(category.get());
  }

  private CategoryDTO entityToDTO(Category category) {
    return CategoryDTO.builder()
        .id(category.getId())
        .title(category.getTitle())
        .type(category.getType())
        .build();
  }

}
