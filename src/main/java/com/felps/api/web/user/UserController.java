package com.felps.api.web.user;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.felps.api.model.UserAccount;
import com.felps.api.service.UserService;
import com.felps.api.web.user.dto.CreateUserForm;
import com.felps.api.web.user.dto.UpdateUserForm;
import com.felps.api.web.user.dto.UserDTO;

@RestController
@RequestMapping("/users")
public class UserController {
  @Autowired
  private UserService userService;

  @PostMapping
  public ResponseEntity<Object> store(@RequestBody @Valid CreateUserForm form) {
    UserAccount newUser = userService.createUser(form);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(UserDTO.builder().id(newUser.getId()).name(newUser.getName()).email(newUser.getEmail())
            .active(newUser.isActive()).build());
  }

  @GetMapping
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<List<UserDTO>> list() {
    var users = userService.listAll();

    return ResponseEntity
        .ok(users.stream()
            .map(user -> UserDTO.builder().id(user.getId()).name(user.getName()).email(user.getEmail())
                .active(user.isActive()).build())
            .collect(Collectors.toList()));
  }

  @PutMapping("/{userId}")
  public ResponseEntity<Object> update(@RequestBody @Valid UpdateUserForm form,
      @PathVariable UUID userId) {
    UserAccount updatedUser = userService.updateUser(userId, form);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(UserDTO.builder().id(updatedUser.getId())
        .name(updatedUser.getName()).email(updatedUser.getEmail()).active(updatedUser.isActive()).build());

  }
}
