package com.felps.api.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.felps.api.model.Category;
import com.felps.api.model.Transaction;
import com.felps.api.model.UserAccount;
import com.felps.api.repository.CategoryRepository;
import com.felps.api.repository.TransactionRepository;
import com.felps.api.web.transaction.dto.TransactionForm;

@Service
public class TransactionService {
  @Autowired
  private TransactionRepository transactionRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  public List<Transaction> findAll(UserAccount user) {
    return transactionRepository.findByUser(user);
  }

  public Transaction create(TransactionForm form, UserAccount user) {
    UserAccount loggedUser = new UserAccount();
    Optional<Category> category = categoryRepository.findById(form.getCategoryId());

    if (!category.isPresent()) {
      throw new RuntimeException("Category not exists");
    }

    loggedUser.setId(user.getId());

    Transaction newTransaction = new Transaction();

    BeanUtils.copyProperties(form, newTransaction);
    newTransaction.setUser(loggedUser);
    newTransaction.setCategory(category.get());

    return transactionRepository.save(newTransaction);
  }

  public Transaction update(TransactionForm form, UUID transactionId, UserAccount user) {
    UserAccount loggedUser = new UserAccount();
    Optional<Transaction> transaction = transactionRepository.findById(transactionId);
    Optional<Category> category = categoryRepository.findById(form.getCategoryId());

    if (!transaction.isPresent()) {
      throw new RuntimeException("Transaction not exists");
    }

    if (!category.isPresent()) {
      throw new RuntimeException("Category not exists");
    }
    if (!transaction.get().getUser().getId().equals(user.getId())) {
      throw new RuntimeException("Not have permissions");
    }

    loggedUser.setId(user.getId());

    Transaction newTransaction = new Transaction();

    BeanUtils.copyProperties(form, newTransaction);
    newTransaction.setUser(loggedUser);
    newTransaction.setCategory(category.get());
    newTransaction.setUser(loggedUser);
    newTransaction.setId(transactionId);

    return transactionRepository.save(newTransaction);
  }

  public void delete(UUID transactionId, UserAccount user) {
    Optional<Transaction> transaction = transactionRepository.findById(transactionId);

    if (!transaction.isPresent()) {
      throw new RuntimeException("Transaction Not Exists");
    }

    if (!transaction.get().getUser().getId().equals(user.getId())) {
      throw new RuntimeException("Not have permissions");
    }

    transactionRepository.delete(transaction.get());
  }

}
