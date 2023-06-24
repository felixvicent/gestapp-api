package com.felps.api.web.transaction.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.felps.api.web.category.dto.CategoryDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionDTO {
  private UUID id;

  private String description;

  private double value;

  private LocalDateTime datetime;

  private CategoryDTO category;
}
