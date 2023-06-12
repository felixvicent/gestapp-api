package com.felps.api.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
public class Role implements GrantedAuthority {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  private String name;

  public Role(UUID id) {
    this.id = id;
  }

  @Override
  public String getAuthority() {
    return this.name;
  }
}
