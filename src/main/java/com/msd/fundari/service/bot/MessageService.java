package com.msd.fundari.service.bot;

import com.msd.fundari.bot.FundariBot;
import com.msd.fundari.bot.keyboard.MainKeyboards;
import com.msd.fundari.entity.UserEntity;
import com.msd.fundari.repository.UserRepository;
import com.msd.fundari.service.UserService;
import com.msd.fundari.service.bot.redis.BotStateService;
import com.msd.fundari.utils.annotation.BotStateController;
import com.msd.fundari.utils.annotation.BotStateHandler;
import com.msd.fundari.utils.enums.Languages;
import com.msd.fundari.utils.enums.ProjectType;
import com.msd.fundari.utils.telegram.BotState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Service
@RequiredArgsConstructor
public class MessageService {
  private final BotStateService botStateService;
  private final MainKeyboards mainKeyboards;

  public void startEvaluation(final FundariBot fundariBot, final Message message, final String lang) {
    Long chatId = message.getChatId();

    botStateService.setState(chatId, BotState.PROJECT_TYPE);

    fundariBot.sendMessage(
        chatId, "What kind of project is this?", mainKeyboards.serviceKeyboard(lang));
  }

  public void startEvaluation(
      final FundariBot fundariBot, final Message message, final BotState state, final String lang) {
    Long chatId = message.getChatId();

    botStateService.setState(chatId, state);

    fundariBot.sendMessage(
        chatId,
        "Please enter the name of your project" + "(This will appear in your final report.)");
  }

  // TODO
  public void contactSupport(final FundariBot fundariBot, final Message message) {}

  public void changeLanguage(final FundariBot fundariBot, final Long chatId) {
    botStateService.setState(chatId, BotState.LANGUAGE_SELECT);
    fundariBot.sendMessage(chatId, "Выберите язык:", mainKeyboards.languageKeyboard());
  }
}
