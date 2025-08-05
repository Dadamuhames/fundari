package com.msd.fundari.bot.handler;

import com.msd.fundari.bot.FundariBot;
import com.msd.fundari.bot.keyboard.MainKeyboards;
import com.msd.fundari.service.bot.redis.BotStateService;
import com.msd.fundari.utils.enums.Languages;
import com.msd.fundari.utils.telegram.BaseBotInterface;
import com.msd.fundari.utils.telegram.BotState;
import com.msd.fundari.utils.telegram.UpdateHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class CommandHandler implements UpdateHandler {
  private final BotStateService botStateService;
  private final MainKeyboards mainKeyboards;

  @Override
  public void handleUpdate(
      final Update update, final BaseBotInterface bot, final Languages language) {
    FundariBot fundariBot = (FundariBot) bot;

    Long chatId = update.getMessage().getChatId();
    String message = update.getMessage().getText();

    switch (message) {
      case "/start" -> {
        botStateService.setState(chatId, BotState.IDLE);
        fundariBot.sendMessage(
            chatId,
            "Веберите секцию:",
            mainKeyboards.idleKeyboard(language.toString().toLowerCase()));
      }

      default -> {}
    }
  }
}
