package com.felps.api.exceptions;

public class NoHasPermissionException extends RuntimeException {
  public NoHasPermissionException(String message) {
    super(message);
  }
}
