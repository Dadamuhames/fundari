package com.msd.fundari.service.bot.redis;

import com.msd.fundari.entity.redis.BotStateEntity;
import com.msd.fundari.repository.redis.BotStateRepository;
import com.msd.fundari.utils.telegram.BotState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotStateService {
  private final BotStateRepository botStateRepository;

  public BotStateEntity getInstance(final Long chatId) {
    BotStateEntity userState = botStateRepository.findById(chatId).orElse(null);

    if (userState == null) {
      userState = BotStateEntity.builder().telegramChatId(chatId).state(BotState.LANGUAGE_SELECT).build();
      return botStateRepository.save(userState);
    }

    return userState;
  }

  public BotState getState(final Long chatId) {
    BotStateEntity userState = getInstance(chatId);

    return userState.getState();
  }

  public void setState(final Long chatId, final BotState state) {
    BotStateEntity userState = getInstance(chatId);

    userState.setState(state);

    botStateRepository.save(userState);
  }
}
