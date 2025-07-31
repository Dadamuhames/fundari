package com.msd.fundari.service.bot;

import com.msd.fundari.bot.FundariBot;
import com.msd.fundari.bot.keyboard.MainKeyboards;
import com.msd.fundari.entity.UserEntity;
import com.msd.fundari.repository.UserRepository;
import com.msd.fundari.service.UserService;
import com.msd.fundari.service.bot.redis.BotStateService;
import com.msd.fundari.utils.enums.Languages;
import com.msd.fundari.utils.telegram.BotState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Service
@RequiredArgsConstructor
public class MessageService {
  private final UserRepository userRepository;
  private final UserService userService;
  private final BotStateService botStateService;

  public void handleLanguageSelect(final FundariBot fundariBot, final Message message) {
    Long chatId = message.getChatId();

    UserEntity user = userService.getUserByChatId(chatId);

    Languages language = (Languages) Languages.EN.valueOfLabel(message.getText());

    if (language == null) {
      fundariBot.sendMessage(chatId, "Выберите язык:", MainKeyboards.languageKeyboard());
      return;
    }

    user.setLanguage(language);

    userRepository.save(user);

    botStateService.setState(chatId, BotState.IDLE);
    fundariBot.sendMessage(chatId, "Веберите секцию:", MainKeyboards.idleKeyboard());
  }

  public void handleSectionSelect(final FundariBot fundariBot, final Message message) {
    Long chatId = message.getChatId();

    String messageText = message.getText();

    switch (messageText) {
      case "\uD83E\uDDEE Evaluate project" -> startEvaluation(fundariBot, message);

      case "\uD83D\uDCE9 Contact Support" -> contactSupport(fundariBot, message);

      case "\uD83C\uDF10 Change Language" -> changeLanguage(fundariBot, chatId);
      case null, default -> {}
    }
  }

  public void startEvaluation(final FundariBot fundariBot, final Message message) {
    Long chatId = message.getChatId();

    botStateService.setState(chatId, BotState.BUSINESS_PROJECT_NAME);

    fundariBot.sendMessage(
        chatId,
        "Please enter the name of your business, startup or idea.\n"
            + "(This will appear in your final report.)");
  }

  // TODO
  public void contactSupport(final FundariBot fundariBot, final Message message) {}

  public void changeLanguage(final FundariBot fundariBot, final Long chatId) {
    botStateService.setState(chatId, BotState.LANGUAGE_SELECT);
    fundariBot.sendMessage(chatId, "Выберите язык:", MainKeyboards.languageKeyboard());
  }
}
