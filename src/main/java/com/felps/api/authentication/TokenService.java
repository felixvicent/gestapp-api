package com.felps.api.authentication;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.felps.api.model.UserAccount;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {

  @Value("${jwt.secret}")
  private String secret;

  public String generateToken(Authentication authentication) {
    UserAccount user = (UserAccount) authentication.getPrincipal();

    return generateToken(user);
  }

  public String generateToken(UserAccount user) {
    return Jwts.builder().setIssuer("GestAPP").setSubject(String.valueOf(user.getId())).claim("roles",
        user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()).setIssuedAt(new Date())
        .signWith(SignatureAlgorithm.HS256, secret).compact();
  }

  public boolean isTokenValid(String token) {
    try {
      Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public UUID getTokenId(String token) {
    Claims body = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    return UUID.fromString(body.getSubject());
  }
}
