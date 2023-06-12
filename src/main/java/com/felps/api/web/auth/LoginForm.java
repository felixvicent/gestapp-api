package com.felps.api.web.auth;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class LoginForm {

  @NotBlank
  @Email
  private String email;

  @NotBlank
  private String password;
}
