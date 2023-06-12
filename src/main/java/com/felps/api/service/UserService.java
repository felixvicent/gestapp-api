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

    var userRoles = userAccount.getRoles();

    var role = new Role(form.getRole().getId());

    userRoles.add(role);

    userAccount.setRoles(userRoles);

    return userRepository.save(userAccount);
  }

  public List<UserAccount> listAll() {
    return userRepository.findAll();
  }

}
