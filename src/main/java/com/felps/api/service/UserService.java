package com.felps.api.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.felps.api.model.Role;
import com.felps.api.model.UserAccount;
import com.felps.api.repository.UserRepository;
import com.felps.api.web.user.CreateUserForm;
import com.felps.api.web.user.UpdateUserForm;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder encoder;

  public Optional<UserAccount> loadForAuthentication(String email) {
    return userRepository.findByEmail(email);
  }

  public Optional<UserAccount> loadForAuthenticationById(UUID id) {
    return userRepository.findById(id);
  }

  @Transactional
  public UserAccount createUser(CreateUserForm form) {
    if (this.loadForAuthentication(form.getEmail()).isPresent()) {
      throw new RuntimeException("User already Exists");
    }

    var userAccount = new UserAccount();
    BeanUtils.copyProperties(form, userAccount);
    userAccount.setPassword(encoder.encode(form.getPassword()));

    var role = new Role(form.getRole().getId());

    userAccount.getRoles().add(role);

    return userRepository.save(userAccount);
  }

  public List<UserAccount> listAll() {
    return userRepository.findAll();
  }

  @Transactional
  public UserAccount updateUser(UUID userId, UpdateUserForm form) {
    Optional<UserAccount> user = userRepository.findById(userId);

    if (!user.isPresent()) {
      throw new RuntimeException("User Not Exists");
    }

    var userAccount = new UserAccount();

    BeanUtils.copyProperties(form, userAccount);
    userAccount.setId(userId);

    var role = new Role(form.getRole().getId());

    userAccount.getRoles().add(role);
    userAccount.setPassword(user.get().getPassword());

    return userRepository.save(userAccount);
  }

}
