package com.msd.fundari.repository;

import com.msd.fundari.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

  boolean existsByTelegramId(final Long telegramId);

  Optional<UserEntity> findByTelegramId(final Long telegramId);
}
