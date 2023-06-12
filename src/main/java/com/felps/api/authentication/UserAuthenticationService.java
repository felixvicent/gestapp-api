package com.felps.api.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.felps.api.model.UserAccount;
import com.felps.api.service.UserService;

@Component
public class UserAuthenticationService implements UserDetailsService {

  @Autowired
  private UserService userService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserAccount user = userService.loadForAuthentication(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    return user;
  }
}
