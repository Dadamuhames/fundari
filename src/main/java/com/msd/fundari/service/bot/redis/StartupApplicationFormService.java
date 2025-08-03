package com.msd.fundari.service.bot.redis;

import com.msd.fundari.entity.redis.StartupApplicationForm;
import com.msd.fundari.repository.redis.StartupApplicationFormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StartupApplicationFormService {
  private final StartupApplicationFormRepository startupApplicationFormRepository;

  public StartupApplicationForm getForm(final Long chatId) {
    return startupApplicationFormRepository
        .findById(chatId)
        .orElse(StartupApplicationForm.builder().telegramId(chatId).build());
  }

  public void saveForm(final Long chatId, final StartupApplicationForm form) {
    form.setTelegramId(chatId);
    startupApplicationFormRepository.save(form);
  }

  public void clearForm(final Long chatId) {
    startupApplicationFormRepository.deleteById(chatId);
  }
}
