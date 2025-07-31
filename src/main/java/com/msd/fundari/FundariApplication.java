package com.msd.fundari;

import com.msd.fundari.bot.FundariBot;
import com.msd.fundari.config.properties.TelegramProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.webhook.TelegramBotsWebhookApplication;
import org.telegram.telegrambots.webhook.WebhookOptions;

@Slf4j
@SpringBootApplication
public class FundariApplication implements CommandLineRunner {
  @Autowired public FundariBot fundariBot;

  public static void main(String[] args) {
    SpringApplication.run(FundariApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {

  }
}
