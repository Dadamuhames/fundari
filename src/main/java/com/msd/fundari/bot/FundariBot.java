package com.msd.fundari.bot;

import com.msd.fundari.bot.handler.MainHandler;
import com.msd.fundari.utils.telegram.BaseBot;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.DeleteWebhook;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.webhook.TelegramWebhookBot;

@Slf4j
@Component
public class FundariBot extends BaseBot implements TelegramWebhookBot {
  @Autowired public MainHandler mainHandler;
  @Autowired public SetWebhook setWebhook;

  public FundariBot(String botToken) {
    super(botToken);
  }

  @Override
  public void runDeleteWebhook() {
    executeMethod(new DeleteWebhook());
  }

  @Override
  public void runSetWebhook() {
    executeMethod(setWebhook);
  }

  @PostConstruct
  public void initWebhook() {
    log.info("Registering Telegram webhook...");
    runDeleteWebhook(); // optional: clears old one
    runSetWebhook();    // must be explicitly called
  }

  @Override
  public BotApiMethod<?> consumeUpdate(Update update) {
    mainHandler.handleUpdate(update, this);

    return null;
  }

  @Override
  public String getBotPath() {
    return "/api/v1/webhook/telegram";
  }
}
