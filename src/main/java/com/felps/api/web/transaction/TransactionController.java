package com.felps.api.web.transaction;

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

import com.felps.api.model.UserAccount;
import com.felps.api.service.TransactionService;
import com.felps.api.web.transaction.dto.TransactionDTO;
import com.felps.api.web.transaction.dto.TransactionForm;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
  @Autowired
  private TransactionService transactionService;

  @GetMapping
  public ResponseEntity<Page<TransactionDTO>> index(
      @PageableDefault(page = 0, size = 10, sort = "datetime", direction = Sort.Direction.DESC) Pageable pageable,
      @AuthenticationPrincipal UserAccount user) {
    Page<TransactionDTO> transactions = transactionService.findAll(user, pageable);

    return ResponseEntity.ok(transactions);
  }

  @PostMapping
  public ResponseEntity<TransactionDTO> store(@AuthenticationPrincipal UserAccount user,
      @RequestBody @Valid TransactionForm form) {

    TransactionDTO newTransaction = transactionService.create(form, user);

    return ResponseEntity.status(HttpStatus.CREATED).body(newTransaction);

  }

  @PutMapping("/{transactionId}")
  public ResponseEntity<TransactionDTO> update(@AuthenticationPrincipal UserAccount user,
      @RequestBody @Valid TransactionForm form, @PathVariable UUID transactionId) {
    TransactionDTO updatedTransaction = transactionService.update(form, transactionId, user);

    return ResponseEntity.status(HttpStatus.OK)
        .body(updatedTransaction);

  }

  @DeleteMapping("/{transactionId}")
  public ResponseEntity<?> delete(@AuthenticationPrincipal UserAccount user,
      @PathVariable UUID transactionId) {

    transactionService.delete(transactionId, user);

    return ResponseEntity.noContent().build();

  }
}
