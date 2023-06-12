package com.felps.api.web.auth;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.felps.api.authentication.TokenService;
import com.felps.api.model.Role;
import com.felps.api.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private TokenService tokenService;

  @Autowired
  private UserService userService;

  @PostMapping
  public ResponseEntity<?> auth(@RequestBody @Valid LoginForm form) {
    var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(form.getEmail(),
        form.getPassword());

    var authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

    if (!authentication.isAuthenticated()) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    String token = tokenService.generateToken(authentication);
    var user = userService.loadForAuthentication(form.getEmail())
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    return new ResponseEntity<>(TokenDTO.builder().type("Bearer").token(token)
        .roles(user.getRoles().stream().map(Role::getName).toList()).build(), HttpStatus.OK);

  }
}
