package com.msd.fundari.bot.controller;

import com.msd.fundari.bot.FundariBot;
import com.msd.fundari.bot.keyboard.MainKeyboards;
import com.msd.fundari.bot.keyboard.StartupApplicationKeyboard;
import com.msd.fundari.entity.ApplicationEntity;
import com.msd.fundari.entity.redis.StartupApplicationForm;
import com.msd.fundari.model.ai.output.StartupEvalOutput;
import com.msd.fundari.service.ai.StartupEvalAiService;
import com.msd.fundari.service.bot.ApplicationService;
import com.msd.fundari.service.bot.redis.BotStateService;
import com.msd.fundari.service.bot.redis.StartupApplicationFormService;
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

  @BotStateHandler(BotState.START_UP_PROJECT_NAME)
  public void processProjectName(final FundariBot fundariBot, final Message message) {
    Long chatId = message.getChatId();
    String name = message.getText();

    StartupApplicationForm form = startupApplicationFormService.getForm(chatId);
    form.setProjectName(name);
    startupApplicationFormService.saveForm(chatId, form);

    botStateService.setState(chatId, BotState.START_UP_STAGE);
    fundariBot.sendMessage(
        chatId,
        "На какой стадии сейчас твой стартап?",
        StartupApplicationKeyboard.startupStageKeyboard());
  }

  @BotStateHandler(BotState.START_UP_STAGE)
  public void processStage(final FundariBot fundariBot, final Message message) {
    Long chatId = message.getChatId();

    StartupStage stage = (StartupStage) StartupStage.IDEA.valueOfLabel(message.getText());

    if (stage == null) {
      fundariBot.sendMessage(
          chatId,
          "На какой стадии сейчас твой стартап? Выберите вариант ниже:",
          StartupApplicationKeyboard.startupStageKeyboard());
      return;
    }

    StartupApplicationForm form = startupApplicationFormService.getForm(chatId);
    form.setStage(stage);
    startupApplicationFormService.saveForm(chatId, form);

    botStateService.setState(chatId, BotState.START_UP_DESC);
    fundariBot.sendMessage(
        chatId, "Опиши кратко, чем занимается стартап. (Можно в 1-2 предложениях)");
  }

  @BotStateHandler(BotState.START_UP_DESC)
  public void processDesc(final FundariBot fundariBot, final Message message) {
    Long chatId = message.getChatId();

    String desc = message.getText();

    StartupApplicationForm form = startupApplicationFormService.getForm(chatId);
    form.setDescription(desc);
    startupApplicationFormService.saveForm(chatId, form);

    botStateService.setState(chatId, BotState.START_UP_HAS_LAST_MONTH_PROFIT);
    fundariBot.sendMessage(
        chatId, "Есть ли у вас выручка за последний месяц?", MainKeyboards.yesNoKeyboard());
  }

  @BotStateHandler(BotState.START_UP_HAS_LAST_MONTH_PROFIT)
  public void processHasLastMonthProfit(final FundariBot fundariBot, final Message message) {
    Long chatId = message.getChatId();

    String answer = message.getText();

    if (!ValidationUtils.isYesOrNo(answer, fundariBot, chatId)) {
      return;
    }

    boolean hasProfit = answer.equals("Yes");

    if (hasProfit) {
      botStateService.setState(chatId, BotState.START_UP_LAST_MONTH_PROFIT);
      fundariBot.sendMessage(chatId, "Сколько составила выручка в USD?");
    } else {
      botStateService.setState(chatId, BotState.START_UP_ACTIVE_USERS);
      fundariBot.sendMessage(chatId, "Сколько всего у вас активных пользователей?");
    }
  }

  @BotStateHandler(BotState.START_UP_LAST_MONTH_PROFIT)
  public void processLastMonthProfit(final FundariBot fundariBot, final Message message) {
    Long chatId = message.getChatId();

    String profit = message.getText();

    if (!ValidationUtils.isNumeric(profit)) {
      fundariBot.sendMessage(chatId, "Сколько составила выручка в USD?");
      return;
    }

    BigDecimal profitBigDec = new BigDecimal(profit);

    StartupApplicationForm form = startupApplicationFormService.getForm(chatId);
    form.setLastMonthRevenue(profitBigDec);
    startupApplicationFormService.saveForm(chatId, form);

    botStateService.setState(chatId, BotState.START_UP_ACTIVE_USERS);
    fundariBot.sendMessage(chatId, "Сколько всего у вас активных пользователей?");
  }

  @BotStateHandler(BotState.START_UP_ACTIVE_USERS)
  public void processActiveUserCount(final FundariBot fundariBot, final Message message) {
    Long chatId = message.getChatId();

    String userCount = message.getText();

    if (!ValidationUtils.isInteger(userCount)) {
      fundariBot.sendMessage(chatId, "Сколько всего у вас активных пользователей?");
      return;
    }

    Integer userCountInt = Integer.valueOf(userCount);

    StartupApplicationForm form = startupApplicationFormService.getForm(chatId);
    form.setActiveUserCount(userCountInt);
    startupApplicationFormService.saveForm(chatId, form);

    botStateService.setState(chatId, BotState.START_UP_INVESTMENT);
    fundariBot.sendMessage(chatId, "Сколько было инвестиций в стартап на текущий момент?");
  }

  @BotStateHandler(BotState.START_UP_INVESTMENT)
  public void processInvestment(final FundariBot fundariBot, final Message message) {
    Long chatId = message.getChatId();

    String investment = message.getText();

    if (!ValidationUtils.isNumeric(investment)) {
      botStateService.setState(chatId, BotState.START_UP_INVESTMENT);
      fundariBot.sendMessage(chatId, "Сколько было инвестиций в стартап на текущий момент?");
    }

    BigDecimal investmentAmount = new BigDecimal(investment);

    StartupApplicationForm form = startupApplicationFormService.getForm(chatId);
    form.setInvestedMoneyAmount(investmentAmount);
    startupApplicationFormService.saveForm(chatId, form);

    botStateService.setState(chatId, BotState.START_UP_TEAM_SIZE);
    fundariBot.sendMessage(chatId, "Сколько сотрудников работает в проекте (включая фаундеров)?");
  }

  @BotStateHandler(BotState.START_UP_TEAM_SIZE)
  public void processTeamSize(final FundariBot fundariBot, final Message message) {
    Long chatId = message.getChatId();

    String teamSizeStr = message.getText();

    if (!ValidationUtils.isInteger(teamSizeStr)) {
      fundariBot.sendMessage(chatId, "Сколько сотрудников работает в проекте (включая фаундеров)?");
      return;
    }

    Integer teamSize = Integer.valueOf(teamSizeStr);

    StartupApplicationForm form = startupApplicationFormService.getForm(chatId);
    form.setTeamSize(teamSize);
    startupApplicationFormService.saveForm(chatId, form);

    botStateService.setState(chatId, BotState.START_UP_COMPETITION_INFO);
    fundariBot.sendMessage(chatId, "Кто ваши конкуренты и в чем ваше отличие?");
  }

  @BotStateHandler(BotState.START_UP_COMPETITION_INFO)
  public void processCompetitionInfo(final FundariBot fundariBot, final Message message) {
    Long chatId = message.getChatId();

    String competitionInfo = message.getText();

    StartupApplicationForm form = startupApplicationFormService.getForm(chatId);
    form.setCompetitors(competitionInfo);
    startupApplicationFormService.saveForm(chatId, form);

    botStateService.setState(chatId, BotState.START_UP_REGION_OF_ACTIVITY);
    fundariBot.sendMessage(chatId, "Где работает стартап?");
  }

  @BotStateHandler(BotState.START_UP_REGION_OF_ACTIVITY)
  public void processRegionOfActivity(final FundariBot fundariBot, final Message message) {
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

    String report = aiOutput.toString(form.getProjectName());

    fundariBot.sendMessage(chatId, report, MainKeyboards.responseKeyboard());

    startupApplicationFormService.clearForm(chatId);
  }
}
