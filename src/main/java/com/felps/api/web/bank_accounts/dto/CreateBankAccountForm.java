package com.felps.api.web.bank_accounts.dto;

import com.felps.api.model.UserAccount;

import lombok.Data;

@Data
public class CreateBankAccountForm {
  private String name;

  private Double initialBalance;

  private UserAccount user;

}
