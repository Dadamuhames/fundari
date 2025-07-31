package com.msd.fundari.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "telegram.bot")
@Getter
@Setter
public class TelegramProperties {
  private String token;

  private String webhookUrl;

  private String webhookPath;

  private String secret;
}
