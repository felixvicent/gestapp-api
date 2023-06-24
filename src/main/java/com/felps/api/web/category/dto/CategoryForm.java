package com.felps.api.web.category.dto;

import javax.validation.constraints.NotBlank;

import com.felps.api.model.CategoryType;

import lombok.Data;

@Data
public class CategoryForm {
  @NotBlank
  private String title;

  private CategoryType type;
}
