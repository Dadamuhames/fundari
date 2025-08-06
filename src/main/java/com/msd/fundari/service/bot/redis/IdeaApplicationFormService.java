package com.msd.fundari.service.bot.redis;

import com.msd.fundari.entity.redis.IdeaApplicationForm;
import com.msd.fundari.entity.redis.StartupApplicationForm;
import com.msd.fundari.repository.redis.IdeaApplicationFormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdeaApplicationFormService {
  private final IdeaApplicationFormRepository ideaApplicationFormRepository;

  public IdeaApplicationForm getForm(final Long chatId) {
    return ideaApplicationFormRepository
        .findById(chatId)
        .orElse(IdeaApplicationForm.builder().telegramId(chatId).build());
  }

  public void saveForm(final Long chatId, final IdeaApplicationForm form) {
    form.setTelegramId(chatId);
    ideaApplicationFormRepository.save(form);
  }

  public void clearForm(final Long chatId) {
    ideaApplicationFormRepository.deleteById(chatId);
  }
}
