package com.felps.api.web.transaction;

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

import com.felps.api.model.Transaction;
import com.felps.api.model.UserAccount;
import com.felps.api.service.TransactionService;
import com.felps.api.web.category.dto.CategoryDTO;
import com.felps.api.web.transaction.dto.TransactionDTO;
import com.felps.api.web.transaction.dto.TransactionForm;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
  @Autowired
  private TransactionService transactionService;

  @GetMapping
  public ResponseEntity<List<TransactionDTO>> index(@AuthenticationPrincipal UserAccount user) {
    List<Transaction> transactions = transactionService.findAll(user);

    return ResponseEntity.ok(transactions.stream().map(
        transaction -> parseEntityToDTO(transaction)).collect(Collectors.toList()));
  }

  @PostMapping
  public ResponseEntity<TransactionDTO> store(@AuthenticationPrincipal UserAccount user,
      @RequestBody @Valid TransactionForm form) {

    Transaction newTransaction = transactionService.create(form, user);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(parseEntityToDTO(newTransaction));

  }

  @PutMapping("/{transactionId}")
  public ResponseEntity<TransactionDTO> update(@AuthenticationPrincipal UserAccount user,
      @RequestBody @Valid TransactionForm form, @PathVariable UUID transactionId) {
    Transaction updatedTransaction = transactionService.update(form, transactionId, user);

    return ResponseEntity.status(HttpStatus.OK)
        .body(parseEntityToDTO(updatedTransaction));

  }

  @DeleteMapping("/{transactionId}")
  public ResponseEntity<?> delete(@AuthenticationPrincipal UserAccount user,
      @PathVariable UUID transactionId) {

    transactionService.delete(transactionId, user);

    return ResponseEntity.noContent().build();

  }

  private TransactionDTO parseEntityToDTO(Transaction transaction) {
    return TransactionDTO.builder()
        .id(transaction.getId())
        .description(transaction.getDescription())
        .value(transaction.getValue())
        .datetime(transaction.getDatetime())
        .category(CategoryDTO.builder()
            .id(transaction.getCategory().getId())
            .title(transaction.getCategory().getTitle())
            .type(transaction.getCategory().getType())
            .build())
        .build();
  }
}
