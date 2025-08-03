package com.msd.fundari.bot.handler;

import com.msd.fundari.bot.FundariBot;
import com.msd.fundari.bot.keyboard.MainKeyboards;
import com.msd.fundari.service.bot.redis.BotStateService;
import com.msd.fundari.service.UserService;
import com.msd.fundari.utils.telegram.BaseBotInterface;
import com.msd.fundari.utils.telegram.BotState;
import com.msd.fundari.utils.telegram.UpdateHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Slf4j
@Component
@RequiredArgsConstructor
public class MainHandler implements UpdateHandler {
  private final BotStateService botStateService;
  private final UserService userService;
  private final MessageHandler messageHandler;
  private final CommandHandler commandHandler;

  @Override
  public void handleUpdate(final Update update, final BaseBotInterface bot) {
    Message message = update.getMessage();

    FundariBot fundariBot = (FundariBot) bot;

    Long chatId =
        message != null ? message.getChatId() : update.getCallbackQuery().getMessage().getChatId();

    boolean isAuthenticated = userService.isAuthenticated(chatId);

    BotState state = botStateService.getState(chatId);

    if (!isAuthenticated && !state.equals(BotState.LANGUAGE_SELECT)) {
      botStateService.setState(chatId, BotState.LANGUAGE_SELECT);
      fundariBot.sendMessage(chatId, "Выберите язык:", MainKeyboards.languageKeyboard());
      return;
    }

    if (message != null && message.isCommand()) {
      commandHandler.handleUpdate(update, fundariBot);
    } else if (update.hasMessage()) {
      messageHandler.handleUpdate(update, fundariBot);
    }
  }
}
