package com.felps.api.web.transaction.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Data;

@Data
public class TransactionForm {
  @NotBlank
  private String description;

  @Positive
  private double value;

  private LocalDateTime datetime;

  @NotNull
  private UUID categoryId;
}
