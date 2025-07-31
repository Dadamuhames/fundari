package com.msd.fundari.utils.enums;


import lombok.Getter;

@Getter
public enum ExceptionType {
  USER_NOT_FOUND("User not found");

  private final String message;

  ExceptionType(final String message) {
    this.message = message;
  }
}
