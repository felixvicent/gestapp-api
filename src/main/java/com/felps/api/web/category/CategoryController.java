package com.felps.api.web.category;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
import com.felps.api.web.category.dto.CategoryForm;

@RestController
@RequestMapping("/categories")
public class CategoryController {
  @Autowired
  private CategoryService categoryService;

  @GetMapping
  public ResponseEntity<Page<CategoryDTO>> index(
      @PageableDefault(page = 0, size = 10, sort = "title", direction = Sort.Direction.DESC) Pageable pageable,
      @AuthenticationPrincipal UserAccount user) {
    Page<CategoryDTO> categories = categoryService.findAll(user, pageable);

    return ResponseEntity.ok(categories);
  }

  @PostMapping
  public ResponseEntity<CategoryDTO> store(@AuthenticationPrincipal UserAccount user,
      @RequestBody @Valid CategoryForm form) {

    Category newCategory = categoryService.create(form, user);

    return ResponseEntity.status(HttpStatus.CREATED).body(CategoryDTO.builder().id(newCategory.getId())
        .title(newCategory.getTitle()).type(newCategory.getType()).build());
  }

  @PutMapping("/{categoryId}")
  public ResponseEntity<CategoryDTO> update(@AuthenticationPrincipal UserAccount user,
      @RequestBody @Valid CategoryForm form, @PathVariable UUID categoryId) {

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
