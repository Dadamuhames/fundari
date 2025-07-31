package com.msd.fundari.utils.exception;

import com.msd.fundari.utils.enums.ExceptionType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BotException extends RuntimeException {
  private ExceptionType type;

  public BotException(final ExceptionType type) {
    this.type = type;
  }
}
