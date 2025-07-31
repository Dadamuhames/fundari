package com.msd.fundari.service;

import com.msd.fundari.entity.UserEntity;
import com.msd.fundari.repository.UserRepository;
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
}
