package com.felps.api.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.felps.api.model.BankAccount;
import com.felps.api.model.UserAccount;
import com.felps.api.repository.BankAccountRepository;
import com.felps.api.web.bank_accounts.dto.CreateBankAccountForm;
import com.felps.api.web.bank_accounts.dto.UpdateBankAccountForm;

@Service
public class BankAccountService {
  @Autowired
  private BankAccountRepository bankAccountRepository;

  public List<BankAccount> listAllByUser(UserAccount user) {
    return bankAccountRepository.findAllByUser(user);
  }

  public BankAccount create(CreateBankAccountForm form, UserAccount user) {
    var newBankAccount = new BankAccount();

    BeanUtils.copyProperties(form, newBankAccount);

    UserAccount exUser = new UserAccount();
    exUser.setId(user.getId());

    newBankAccount.setBalance(form.getInitialBalance());
    newBankAccount.setUser(exUser);

    return bankAccountRepository.save(newBankAccount);
  }

  public BankAccount update(UUID bankAccountId, UpdateBankAccountForm form, UserAccount userAccount) {
    Optional<BankAccount> bankAccount = bankAccountRepository.findByIdAndUser(bankAccountId, userAccount);

    if (!bankAccount.isPresent()) {
      throw new RuntimeException("Bank account not exists");
    }

    bankAccount.get().setName(form.getName());

    return bankAccountRepository.save(bankAccount.get());
  }

  public void delete(UUID bankAccountId, UserAccount userAccount) {
    Optional<BankAccount> bankAccount = bankAccountRepository.findByIdAndUser(bankAccountId, userAccount);

    if (!bankAccount.isPresent()) {
      throw new RuntimeException("Bank account not exists");
    }
    bankAccountRepository.delete(bankAccount.get());

  }
}
