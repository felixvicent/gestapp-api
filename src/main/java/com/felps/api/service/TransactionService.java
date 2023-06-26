package com.felps.api.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.felps.api.exceptions.NoHasPermissionException;
import com.felps.api.exceptions.ResourceNotFoundException;
import com.felps.api.model.Category;
import com.felps.api.model.Transaction;
import com.felps.api.model.UserAccount;
import com.felps.api.repository.CategoryRepository;
import com.felps.api.repository.TransactionRepository;
import com.felps.api.web.category.dto.CategoryDTO;
import com.felps.api.web.transaction.dto.TransactionDTO;
import com.felps.api.web.transaction.dto.TransactionForm;

@Service
public class TransactionService {
  @Autowired
  private TransactionRepository transactionRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  public Page<TransactionDTO> findAll(UserAccount user, Pageable pageable) {
    return transactionRepository.findByUser(user, pageable).map(transaction -> entityToDTO(transaction));
  }

  public TransactionDTO create(TransactionForm form, UserAccount user) {
    UserAccount loggedUser = new UserAccount();
    Optional<Category> category = categoryRepository.findById(form.getCategoryId());

    if (!category.isPresent()) {
      throw new ResourceNotFoundException("Category not exists");
    }

    loggedUser.setId(user.getId());

    Transaction newTransaction = new Transaction();

    BeanUtils.copyProperties(form, newTransaction);
    newTransaction.setUser(loggedUser);
    newTransaction.setCategory(category.get());
    newTransaction.setType(category.get().getType());

    return entityToDTO(transactionRepository.save(newTransaction));
  }

  public TransactionDTO update(TransactionForm form, UUID transactionId, UserAccount user) {
    UserAccount loggedUser = new UserAccount();
    Optional<Transaction> transaction = transactionRepository.findById(transactionId);
    Optional<Category> category = categoryRepository.findById(form.getCategoryId());

    if (!transaction.isPresent()) {
      throw new ResourceNotFoundException("Transaction not found");
    }

    if (!category.isPresent()) {
      throw new ResourceNotFoundException("Category not exists");
    }
    if (!transaction.get().getUser().getId().equals(user.getId())) {
      throw new NoHasPermissionException("Not have permissions");
    }

    loggedUser.setId(user.getId());

    Transaction newTransaction = new Transaction();

    BeanUtils.copyProperties(form, newTransaction);
    newTransaction.setUser(loggedUser);
    newTransaction.setCategory(category.get());
    newTransaction.setUser(loggedUser);
    newTransaction.setId(transactionId);

    return entityToDTO(transactionRepository.save(newTransaction));
  }

  public void delete(UUID transactionId, UserAccount user) {
    Optional<Transaction> transaction = transactionRepository.findById(transactionId);

    if (!transaction.isPresent()) {
      throw new ResourceNotFoundException("Transaction Not Exists");
    }

    if (!transaction.get().getUser().getId().equals(user.getId())) {
      throw new NoHasPermissionException("Not have permissions");
    }

    transactionRepository.delete(transaction.get());
  }

  private TransactionDTO entityToDTO(Transaction transaction) {
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
