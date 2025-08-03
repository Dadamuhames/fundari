package com.msd.fundari.bot.controller;

import com.msd.fundari.bot.FundariBot;
import com.msd.fundari.bot.keyboard.MainKeyboards;
import com.msd.fundari.entity.UserEntity;
import com.msd.fundari.repository.UserRepository;
import com.msd.fundari.service.UserService;
import com.msd.fundari.service.bot.MessageService;
import com.msd.fundari.service.bot.redis.BotStateService;
import com.msd.fundari.utils.annotation.BotStateController;
import com.msd.fundari.utils.annotation.BotStateHandler;
import com.msd.fundari.utils.enums.Languages;
import com.msd.fundari.utils.enums.ProjectType;
import com.msd.fundari.utils.telegram.BotState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Component
@BotStateController
@RequiredArgsConstructor
public class MainStateController {
  private final UserRepository userRepository;
  private final UserService userService;
  private final BotStateService botStateService;
  private final MessageService messageService;

  @BotStateHandler(BotState.LANGUAGE_SELECT)
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

  @BotStateHandler(BotState.IDLE)
  public void handleSectionSelect(final FundariBot fundariBot, final Message message) {
    Long chatId = message.getChatId();

    String messageText = message.getText();

    switch (messageText) {
      case "\uD83E\uDDEE Evaluate project" -> messageService.startEvaluation(fundariBot, message);

      case "\uD83D\uDCE9 Contact Support" -> messageService.contactSupport(fundariBot, message);

      case "\uD83C\uDF10 Change Language" -> messageService.changeLanguage(fundariBot, chatId);

      default -> {}
    }
  }

  @BotStateHandler(BotState.PROJECT_TYPE)
  public void selectService(final FundariBot fundariBot, final Message message) {
    Long chatId = message.getChatId();

    ProjectType projectType = (ProjectType) ProjectType.IDEA.valueOfLabel(message.getText());

    if (projectType == null) {
      fundariBot.sendMessage(
          chatId,
          "What kind of project is this? (Choose from the following)",
          MainKeyboards.serviceKeyboard());
      return;
    }

    switch (projectType) {
      case BUSINESS ->
          messageService.startEvaluation(fundariBot, message, BotState.BUSINESS_PROJECT_NAME);

      case IDEA ->
          messageService.startEvaluation(fundariBot, message, BotState.IDEA_PROJECT_NAME);

      case STARTUP ->
          messageService.startEvaluation(fundariBot, message, BotState.START_UP_PROJECT_NAME);

      default -> {}
    }
  }
}
