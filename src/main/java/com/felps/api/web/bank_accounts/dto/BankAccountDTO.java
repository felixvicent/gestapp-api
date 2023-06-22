package com.felps.api.web.bank_accounts.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BankAccountDTO {
  private UUID id;

  private String name;

  private Double balance;

}
