package com.felps.api.web.category.dto;

import java.util.UUID;

import com.felps.api.model.CategoryType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryDTO {
  private UUID id;

  private String title;

  private CategoryType type;
}
