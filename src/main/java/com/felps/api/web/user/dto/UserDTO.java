package com.felps.api.web.user.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
  private UUID id;

  private String name;

  private String email;

  private Boolean active;
}
