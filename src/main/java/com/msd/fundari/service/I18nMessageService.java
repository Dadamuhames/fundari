package com.msd.fundari.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class I18nMessageService {
  private final MessageSource messageSource;

  public String message(String messageId, String locale) {
    return messageSource.getMessage(messageId, null, Locale.of(locale));
  }
}
