package com.felps.api.web.auth;

import com.felps.api.web.user.dto.UserDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticateDTO {
  private UserDTO user;
  private TokenDTO token;
}
