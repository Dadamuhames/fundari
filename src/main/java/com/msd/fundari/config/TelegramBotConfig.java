package com.msd.fundari.config;

import com.msd.fundari.config.properties.TelegramProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TelegramBotConfig {
  private final TelegramProperties telegramProperties;

  @Bean
  public SetWebhook setWebhook() {
    log.info("Webhook url: {}", telegramProperties.getWebhookUrl());
    log.info("Webhook secret: {}", telegramProperties.getSecret());

    return SetWebhook.builder()
        .url(telegramProperties.getWebhookUrl())
        .secretToken(telegramProperties.getSecret())
        .build();
  }

  @Bean(name = "botToken")
  public String botToken() {
    return telegramProperties.getToken();
  }
}
