package com.felps.api.web.user;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.felps.api.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
  @Autowired
  private UserService userService;

  @PostMapping
  public ResponseEntity<Object> store(@RequestBody @Valid CreateUserForm form) {

    return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(form));
  }

  @GetMapping
  public ResponseEntity<List<UserDTO>> list() {
    var users = userService.listAll();

    return ResponseEntity
        .ok(users.stream()
            .map(user -> UserDTO.builder().id(user.getId()).name(user.getName()).email(user.getEmail())
                .active(user.isActive()).build())
            .collect(Collectors.toList()));
  }
}
