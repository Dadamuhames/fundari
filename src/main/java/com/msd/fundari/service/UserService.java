package com.msd.fundari.service;

import com.msd.fundari.entity.UserEntity;
import com.msd.fundari.repository.UserRepository;
import com.msd.fundari.utils.enums.ExceptionType;
import com.msd.fundari.utils.exception.BotException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  public boolean isAuthenticated(final Long telegramId) {
    return userRepository.existsByTelegramId(telegramId);
  }

  public UserEntity getUserByChatId(final Long telegramId) {
    return userRepository
        .findByTelegramId(telegramId)
        .orElse(UserEntity.builder().telegramId(telegramId).build());
  }

  public UserEntity getUserOrNull(final Long telegramId) {
    return userRepository.findByTelegramId(telegramId).orElse(null);
  }

  public UserEntity getUser(final Long telegramId) {
    return userRepository
        .findByTelegramId(telegramId)
        .orElseThrow(() -> new BotException(ExceptionType.USER_NOT_FOUND));
  }
}
