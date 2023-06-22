package com.felps.api.web.bank_accounts;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.felps.api.model.BankAccount;
import com.felps.api.model.UserAccount;
import com.felps.api.service.BankAccountService;
import com.felps.api.web.bank_accounts.dto.BankAccountDTO;
import com.felps.api.web.bank_accounts.dto.CreateBankAccountForm;
import com.felps.api.web.bank_accounts.dto.UpdateBankAccountForm;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

@RestController
@RequestMapping("/bank_accounts")
public class BankAccountController {

  @Autowired
  private BankAccountService bankAccountService;

  @GetMapping
  public ResponseEntity<List<BankAccountDTO>> list(@AuthenticationPrincipal UserAccount principal) {
    var bankAccounts = bankAccountService.listAllByUser(principal);

    return ResponseEntity
        .ok(bankAccounts.stream().map(
            bankAccount -> BankAccountDTO.builder()
                .id(bankAccount.getId())
                .balance(bankAccount.getBalance())
                .name(bankAccount.getName())
                .build())
            .collect(Collectors.toList()));
  }

  @PostMapping
  public ResponseEntity<Object> create(@AuthenticationPrincipal UserAccount principal,
      @RequestBody CreateBankAccountForm form) {
    try {
      var newAccountBank = bankAccountService.create(form, principal);

      return ResponseEntity.status(HttpStatus.CREATED)
          .body(BankAccountDTO.builder().id(newAccountBank.getId()).name(newAccountBank.getName())
              .balance(newAccountBank.getBalance()).build());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(null);
    }
  }

  @PutMapping("/{bankAccountId}")
  public ResponseEntity<Object> update(@AuthenticationPrincipal UserAccount principal,
      @RequestBody UpdateBankAccountForm form, @PathVariable UUID bankAccountId) {
    try {
      BankAccount updatedBankAccount = bankAccountService.update(bankAccountId, form, principal);

      return ResponseEntity.ok().body(
          BankAccountDTO.builder().id(updatedBankAccount.getId()).balance(updatedBankAccount.getBalance())
              .name(updatedBankAccount.getName()).build());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(null);
    }
  }

  @DeleteMapping("/{bankAccountId}")
  public ResponseEntity<Object> delete(@AuthenticationPrincipal UserAccount principal,
      @PathVariable UUID bankAccountId) {
    try {
      bankAccountService.delete(bankAccountId, principal);

      return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(null);
    }
  }

}
