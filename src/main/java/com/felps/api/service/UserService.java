package com.felps.api.service;

import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.felps.api.exceptions.ResourceAlreadyExistsException;
import com.felps.api.exceptions.ResourceNotFoundException;
import com.felps.api.model.Role;
import com.felps.api.model.UserAccount;
import com.felps.api.repository.UserRepository;
import com.felps.api.web.user.dto.CreateUserForm;
import com.felps.api.web.user.dto.UpdateUserForm;
import com.felps.api.web.user.dto.UserDTO;

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
  public UserDTO createUser(CreateUserForm form) {
    if (this.loadForAuthentication(form.getEmail()).isPresent()) {
      throw new ResourceAlreadyExistsException("User already Exists");
    }

    var userAccount = new UserAccount();
    BeanUtils.copyProperties(form, userAccount);
    userAccount.setPassword(encoder.encode(form.getPassword()));

    var role = new Role(form.getRole().getId());

    userAccount.getRoles().add(role);
    userAccount.setActive(true);

    return entityToDTO(userRepository.save(userAccount));
  }

  public Page<UserDTO> listAll(Pageable pageable) {
    Page<UserDTO> users = userRepository.findAll(pageable).map(user -> entityToDTO(user));

    return users;
  }

  @Transactional
  public UserDTO updateUser(UUID userId, UpdateUserForm form) {
    Optional<UserAccount> user = userRepository.findById(userId);

    if (!user.isPresent()) {
      throw new ResourceNotFoundException("User Not Exists");
    }

    var userAccount = new UserAccount();

    BeanUtils.copyProperties(form, userAccount);
    userAccount.setId(userId);

    var role = new Role(form.getRole().getId());

    userAccount.getRoles().add(role);
    userAccount.setPassword(user.get().getPassword());

    return entityToDTO(userRepository.save(userAccount));
  }

  private UserDTO entityToDTO(UserAccount user) {
    return UserDTO.builder()
        .id(user.getId())
        .name(user.getName())
        .email(user.getEmail())
        .active(user.isActive())
        .role(user.getRoles().iterator().next().getName())
        .createdAt(user.getCreatedAt())
        .build();
  }

}
