package com.msd.fundari.bot.controller;

import com.msd.fundari.bot.FundariBot;
import com.msd.fundari.bot.keyboard.MainKeyboards;
import com.msd.fundari.entity.UserEntity;
import com.msd.fundari.repository.UserRepository;
import com.msd.fundari.service.I18nMessageService;
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
  private final MainKeyboards mainKeyboards;
  private final I18nMessageService i18nMessageService;

  @BotStateHandler(BotState.LANGUAGE_SELECT)
  public void handleLanguageSelect(
      final FundariBot fundariBot, final Message message, final String lang) {
    Long chatId = message.getChatId();

    UserEntity user = userService.getUserByChatId(chatId);

    Languages language = (Languages) Languages.EN.valueOfLabel(message.getText());

    if (language == null) {
      fundariBot.sendMessage(
          chatId,
          "Выберите язык:/Tilni tanlang:/Choose language:",
          mainKeyboards.languageKeyboard());
      return;
    }

    user.setLanguage(language);

    userRepository.save(user);

    botStateService.setState(chatId, BotState.IDLE);

    String chooseSection =
        i18nMessageService.message("chooseSection", language.toString().toLowerCase());
    fundariBot.sendMessage(chatId, chooseSection, mainKeyboards.idleKeyboard(language.toString()));
  }

  @BotStateHandler(BotState.IDLE)
  public void handleSectionSelect(
      final FundariBot fundariBot, final Message message, final String lang) {
    Long chatId = message.getChatId();

    String messageText = message.getText();

    switch (messageText) {
      case "\uD83E\uDDEE Evaluate project",
          "\uD83E\uDDEE Оценить бизнес",
          "\uD83E\uDDEE Biznesni baholash" ->
          messageService.startEvaluation(fundariBot, message, lang);

      case "\uD83D\uDCE9 Contact support",
          "\uD83D\uDCE9 Связаться с поддержкой",
          "\uD83D\uDCE9 Yordam bilan bog'lanish" ->
          messageService.contactSupport(fundariBot, message);

      case "\uD83C\uDF10 Change language",
          "\uD83C\uDF10 Сменить язык",
          "\uD83C\uDF10 Tilni o'zgartirish" ->
          messageService.changeLanguage(fundariBot, chatId);

      default -> {
        String chooseSection = i18nMessageService.message("chooseSection", lang);
        fundariBot.sendMessage(chatId, chooseSection, mainKeyboards.idleKeyboard(lang));
      }
    }
  }

  @BotStateHandler(BotState.PROJECT_TYPE)
  public void selectService(final FundariBot fundariBot, final Message message, final String lang) {
    Long chatId = message.getChatId();

    ProjectType projectType = (ProjectType) ProjectType.IDEA.valueOfLabel(message.getText());

    String selectProjectType = i18nMessageService.message("selectProjectType", lang);

    if (projectType == null) {
      fundariBot.sendMessage(chatId, selectProjectType, mainKeyboards.serviceKeyboard(lang));
      return;
    }

    switch (projectType) {
      case BUSINESS ->
          messageService.startEvaluation(fundariBot, message, BotState.BUSINESS_PROJECT_NAME, lang);

      case IDEA ->
          messageService.startEvaluation(fundariBot, message, BotState.IDEA_PROJECT_NAME, lang);

      case STARTUP ->
          messageService.startEvaluation(fundariBot, message, BotState.START_UP_PROJECT_NAME, lang);

      default -> {}
    }
  }
}
