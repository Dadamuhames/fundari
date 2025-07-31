package com.msd.fundari.service.bot.redis;

import com.msd.fundari.entity.redis.BusinessApplicationForm;
import com.msd.fundari.repository.redis.BusinessApplicationFormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BusinessApplicationFormService {
  private final BusinessApplicationFormRepository businessApplicationFormRepository;

  public BusinessApplicationForm getForm(final Long chatId) {
    return businessApplicationFormRepository
        .findById(chatId)
        .orElse(BusinessApplicationForm.builder().telegramId(chatId).build());
  }

  public void saveForm(final Long chatId, final BusinessApplicationForm form) {
    form.setTelegramId(chatId);
    businessApplicationFormRepository.save(form);
  }

  public void clearForm(final Long chatId) {
    businessApplicationFormRepository.deleteById(chatId);
  }
}
