package com.felps.api.web.category;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.felps.api.model.Category;
import com.felps.api.model.UserAccount;
import com.felps.api.service.CategoryService;
import com.felps.api.web.category.dto.CategoryDTO;
import com.felps.api.web.category.dto.CreateCategoryForm;

@RestController
@RequestMapping("/categories")
public class CategoryController {
  @Autowired
  private CategoryService categoryService;

  @GetMapping
  public ResponseEntity<List<CategoryDTO>> index(@AuthenticationPrincipal UserAccount user) {
    List<Category> categories = categoryService.findAll(user);

    return ResponseEntity.ok(categories.stream().map(category -> CategoryDTO.builder().id(category.getId())
        .title(category.getTitle()).type(category.getType()).build()).collect(Collectors.toList()));
  }

  @PostMapping
  public ResponseEntity<CategoryDTO> store(@AuthenticationPrincipal UserAccount user,
      @RequestBody @Valid CreateCategoryForm form) {

    Category newCategory = categoryService.create(form, user);

    return ResponseEntity.status(HttpStatus.CREATED).body(CategoryDTO.builder().id(newCategory.getId())
        .title(newCategory.getTitle()).type(newCategory.getType()).build());
  }

  @PutMapping("/{categoryId}")
  public ResponseEntity<CategoryDTO> update(@AuthenticationPrincipal UserAccount user,
      @RequestBody @Valid CreateCategoryForm form, @PathVariable UUID categoryId) {

    Category updatedCategory = categoryService.update(form, categoryId, user);

    return ResponseEntity.status(HttpStatus.OK).body(CategoryDTO.builder().id(updatedCategory.getId())
        .title(updatedCategory.getTitle()).type(updatedCategory.getType()).build());
  }

  @DeleteMapping("/{categoryId}")
  public ResponseEntity<?> delete(@AuthenticationPrincipal UserAccount user,
      @PathVariable UUID categoryId) {

    categoryService.delete(categoryId, user);

    return ResponseEntity.noContent().build();
  }
}
