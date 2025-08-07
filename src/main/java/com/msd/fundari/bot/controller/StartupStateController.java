package com.msd.fundari.bot.controller;

import com.msd.fundari.bot.FundariBot;
import com.msd.fundari.bot.keyboard.MainKeyboards;
import com.msd.fundari.bot.keyboard.StartupApplicationKeyboard;
import com.msd.fundari.entity.ApplicationEntity;
import com.msd.fundari.entity.redis.StartupApplicationForm;
import com.msd.fundari.model.ai.output.StartupEvalOutput;
import com.msd.fundari.service.I18nMessageService;
import com.msd.fundari.service.ai.AiOutputSerializerService;
import com.msd.fundari.service.ai.StartupEvalAiService;
import com.msd.fundari.service.bot.ApplicationService;
import com.msd.fundari.service.bot.redis.BotStateService;
import com.msd.fundari.service.bot.redis.StartupApplicationFormService;
import com.msd.fundari.utils.KeyboardValidation;
import com.msd.fundari.utils.ValidationUtils;
import com.msd.fundari.utils.annotation.BotStateController;
import com.msd.fundari.utils.annotation.BotStateHandler;
import com.msd.fundari.utils.enums.StartupStage;
import com.msd.fundari.utils.exception.BotException;
import com.msd.fundari.utils.telegram.BotState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.math.BigDecimal;

@Slf4j
@Component
@BotStateController
@RequiredArgsConstructor
public class StartupStateController {
  private final BotStateService botStateService;
  private final ApplicationService applicationService;
  private final StartupApplicationFormService startupApplicationFormService;
  private final StartupEvalAiService startupEvalAiService;
  private final StartupApplicationKeyboard startupApplicationKeyboard;
  private final MainKeyboards mainKeyboards;
  private final KeyboardValidation keyboardValidation;
  private final I18nMessageService i18nMessageService;
  private final AiOutputSerializerService aiOutputSerializerService;

  @BotStateHandler(BotState.START_UP_PROJECT_NAME)
  public void processProjectName(
      final FundariBot fundariBot, final Message message, final String lang) {
    Long chatId = message.getChatId();
    String name = message.getText();

    StartupApplicationForm form = startupApplicationFormService.getForm(chatId);
    form.setProjectName(name);
    startupApplicationFormService.saveForm(chatId, form);

    botStateService.setState(chatId, BotState.START_UP_STAGE);
    String startupInquireStartupStage =
        i18nMessageService.message("startupInquireStartupStage", lang);
    fundariBot.sendMessage(
        chatId, startupInquireStartupStage, startupApplicationKeyboard.startupStageKeyboard(lang));
  }

  @BotStateHandler(BotState.START_UP_STAGE)
  public void processStage(final FundariBot fundariBot, final Message message, final String lang) {
    Long chatId = message.getChatId();

    StartupStage stage = (StartupStage) StartupStage.IDEA.valueOfLabel(message.getText());

    if (stage == null) {
      String startupInquireStartupStageAgain =
          i18nMessageService.message("startupInquireStartupStageAgain", lang);
      fundariBot.sendMessage(
          chatId,
          startupInquireStartupStageAgain,
          startupApplicationKeyboard.startupStageKeyboard(lang));
      return;
    }

    StartupApplicationForm form = startupApplicationFormService.getForm(chatId);
    form.setStage(stage);
    startupApplicationFormService.saveForm(chatId, form);

    botStateService.setState(chatId, BotState.START_UP_DESC);
    String startupInquireDescription =
        i18nMessageService.message("startupInquireDescription", lang);
    fundariBot.sendMessage(chatId, startupInquireDescription, MainKeyboards.replyKeyboardRemove());
  }

  @BotStateHandler(BotState.START_UP_DESC)
  public void processDesc(final FundariBot fundariBot, final Message message, final String lang) {
    Long chatId = message.getChatId();

    String desc = message.getText();

    StartupApplicationForm form = startupApplicationFormService.getForm(chatId);
    form.setDescription(desc);
    startupApplicationFormService.saveForm(chatId, form);

    botStateService.setState(chatId, BotState.START_UP_HAS_LAST_MONTH_PROFIT);
    String startupInquireHasLastMonthProfit =
        i18nMessageService.message("startupInquireHasLastMonthProfit", lang);
    fundariBot.sendMessage(
        chatId, startupInquireHasLastMonthProfit, mainKeyboards.yesNoKeyboard(lang));
  }

  @BotStateHandler(BotState.START_UP_HAS_LAST_MONTH_PROFIT)
  public void processHasLastMonthProfit(
      final FundariBot fundariBot, final Message message, final String lang) {
    Long chatId = message.getChatId();

    String answer = message.getText();

    if (!keyboardValidation.isYesOrNo(answer, lang)) {
      String startupInquireHasLastMonthProfitAgain =
          i18nMessageService.message("startupInquireHasLastMonthProfitAgain", lang);
      fundariBot.sendMessage(
          chatId, startupInquireHasLastMonthProfitAgain, mainKeyboards.yesNoKeyboard(lang));
      return;
    }

    boolean hasProfit = answer.equals("Yes");

    if (hasProfit) {
      botStateService.setState(chatId, BotState.START_UP_LAST_MONTH_PROFIT);
      String startupInquireLastMonthProfit =
          i18nMessageService.message("startupInquireLastMonthProfit", lang);
      fundariBot.sendMessage(
          chatId, startupInquireLastMonthProfit, MainKeyboards.replyKeyboardRemove());
    } else {
      botStateService.setState(chatId, BotState.START_UP_ACTIVE_USERS);
      String startupInquireActiveUsers =
          i18nMessageService.message("startupInquireActiveUsers", lang);
      fundariBot.sendMessage(
          chatId, startupInquireActiveUsers, MainKeyboards.replyKeyboardRemove());
    }
  }

  @BotStateHandler(BotState.START_UP_LAST_MONTH_PROFIT)
  public void processLastMonthProfit(
      final FundariBot fundariBot, final Message message, final String lang) {
    Long chatId = message.getChatId();

    String profit = message.getText();

    if (!ValidationUtils.isNumeric(profit)) {
      String startupInquireLastMonthProfitAgain =
          i18nMessageService.message("startupInquireLastMonthProfitAgain", lang);
      fundariBot.sendMessage(chatId, startupInquireLastMonthProfitAgain);
      return;
    }

    BigDecimal profitBigDec = new BigDecimal(profit);

    StartupApplicationForm form = startupApplicationFormService.getForm(chatId);
    form.setLastMonthRevenue(profitBigDec);
    startupApplicationFormService.saveForm(chatId, form);

    botStateService.setState(chatId, BotState.START_UP_ACTIVE_USERS);
    String startupInquireActiveUsers =
        i18nMessageService.message("startupInquireActiveUsers", lang);
    fundariBot.sendMessage(chatId, startupInquireActiveUsers, MainKeyboards.replyKeyboardRemove());
  }

  @BotStateHandler(BotState.START_UP_ACTIVE_USERS)
  public void processActiveUserCount(
      final FundariBot fundariBot, final Message message, final String lang) {
    Long chatId = message.getChatId();

    String userCount = message.getText();

    if (!ValidationUtils.isInteger(userCount)) {
      String startupInquireActiveUsersAgain =
          i18nMessageService.message("startupInquireActiveUsersAgain", lang);
      fundariBot.sendMessage(chatId, startupInquireActiveUsersAgain);
      return;
    }

    Integer userCountInt = Integer.valueOf(userCount);

    StartupApplicationForm form = startupApplicationFormService.getForm(chatId);
    form.setActiveUserCount(userCountInt);
    startupApplicationFormService.saveForm(chatId, form);

    botStateService.setState(chatId, BotState.START_UP_INVESTMENT);
    String startupInquireInvestment = i18nMessageService.message("startupInquireInvestment", lang);
    fundariBot.sendMessage(chatId, startupInquireInvestment);
  }

  @BotStateHandler(BotState.START_UP_INVESTMENT)
  public void processInvestment(
      final FundariBot fundariBot, final Message message, final String lang) {
    Long chatId = message.getChatId();

    String investment = message.getText();

    if (!ValidationUtils.isNumeric(investment)) {
      String startupInquireInvestmentAgain =
          i18nMessageService.message("startupInquireInvestmentAgain", lang);
      fundariBot.sendMessage(chatId, startupInquireInvestmentAgain);
      return;
    }

    BigDecimal investmentAmount = new BigDecimal(investment);

    StartupApplicationForm form = startupApplicationFormService.getForm(chatId);
    form.setInvestedMoneyAmount(investmentAmount);
    startupApplicationFormService.saveForm(chatId, form);

    botStateService.setState(chatId, BotState.START_UP_TEAM_SIZE);
    String startupInquireTeamSize = i18nMessageService.message("startupInquireTeamSize", lang);
    fundariBot.sendMessage(chatId, startupInquireTeamSize);
  }

  @BotStateHandler(BotState.START_UP_TEAM_SIZE)
  public void processTeamSize(
      final FundariBot fundariBot, final Message message, final String lang) {
    Long chatId = message.getChatId();

    String teamSizeStr = message.getText();

    if (!ValidationUtils.isInteger(teamSizeStr)) {
      String startupInquireTeamSizeAgain =
          i18nMessageService.message("startupInquireTeamSizeAgain", lang);
      fundariBot.sendMessage(chatId, startupInquireTeamSizeAgain);
      return;
    }

    Integer teamSize = Integer.valueOf(teamSizeStr);

    StartupApplicationForm form = startupApplicationFormService.getForm(chatId);
    form.setTeamSize(teamSize);
    startupApplicationFormService.saveForm(chatId, form);

    botStateService.setState(chatId, BotState.START_UP_COMPETITION_INFO);
    String startupInquireCompetitionInfo =
        i18nMessageService.message("startupInquireCompetitionInfo", lang);
    fundariBot.sendMessage(chatId, startupInquireCompetitionInfo);
  }

  @BotStateHandler(BotState.START_UP_COMPETITION_INFO)
  public void processCompetitionInfo(
      final FundariBot fundariBot, final Message message, final String lang) {
    Long chatId = message.getChatId();

    String competitionInfo = message.getText();

    StartupApplicationForm form = startupApplicationFormService.getForm(chatId);
    form.setCompetitors(competitionInfo);
    startupApplicationFormService.saveForm(chatId, form);

    botStateService.setState(chatId, BotState.START_UP_REGION_OF_ACTIVITY);
    String startupInquireRegionOfActivity =
        i18nMessageService.message("startupInquireRegionOfActivity", lang);
    fundariBot.sendMessage(chatId, startupInquireRegionOfActivity);
  }

  @BotStateHandler(BotState.START_UP_REGION_OF_ACTIVITY)
  public void processRegionOfActivity(
      final FundariBot fundariBot, final Message message, final String lang) {
    Long chatId = message.getChatId();

    String region = message.getText();

    StartupApplicationForm form = startupApplicationFormService.getForm(chatId);
    form.setRegionOfActivity(region);

    ApplicationEntity application;

    // save application
    try {
      application = applicationService.saveStartUpApplication(chatId, form);
    } catch (BotException e) {
      log.error("Message: {}. ChatId: {}", e.getType().getMessage(), chatId);
      return;
    }

    // send the report
    StartupEvalOutput aiOutput = startupEvalAiService.evaluateStartup(application);

    String report =
        aiOutputSerializerService.startupEvalToString(aiOutput, form.getProjectName(), lang);

    fundariBot.sendMessage(chatId, report, mainKeyboards.responseKeyboard(lang));

    startupApplicationFormService.clearForm(chatId);
  }
}
