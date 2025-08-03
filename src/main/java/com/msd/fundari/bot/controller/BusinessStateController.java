package com.msd.fundari.bot.controller;

import com.msd.fundari.bot.FundariBot;
import com.msd.fundari.bot.keyboard.BusinessApplicationKeyboard;
import com.msd.fundari.bot.keyboard.MainKeyboards;
import com.msd.fundari.entity.ApplicationEntity;
import com.msd.fundari.entity.redis.BusinessApplicationForm;
import com.msd.fundari.model.ai.output.BusinessEvalOutput;
import com.msd.fundari.service.ai.BusinessEvalAiService;
import com.msd.fundari.service.bot.ApplicationService;
import com.msd.fundari.service.bot.redis.BotStateService;
import com.msd.fundari.service.bot.redis.BusinessApplicationFormService;
import com.msd.fundari.utils.ValidationUtils;
import com.msd.fundari.utils.annotation.BotStateController;
import com.msd.fundari.utils.annotation.BotStateHandler;
import com.msd.fundari.utils.exception.BotException;
import com.msd.fundari.utils.telegram.BotState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.math.BigDecimal;

@Slf4j
@Component
@BotStateController
@RequiredArgsConstructor
public class BusinessStateController {
  private final BusinessApplicationFormService businessApplicationFormService;
  private final BotStateService botStateService;
  private final ApplicationService applicationService;
  private final BusinessEvalAiService businessEvalAiService;

  @BotStateHandler(BotState.BUSINESS_PROJECT_NAME)
  public void processProjectName(final FundariBot fundariBot, final Message message) {
    Long chatId = message.getChatId();
    String name = message.getText();

    BusinessApplicationForm form = businessApplicationFormService.getForm(chatId);
    form.setProjectName(name);
    businessApplicationFormService.saveForm(chatId, form);

    botStateService.setState(chatId, BotState.BUSINESS_INDUSTRY);
    fundariBot.sendMessage(
        chatId,
        "В какой сфере работает бизнес? (Веберите вариант или введите свой)",
        BusinessApplicationKeyboard.industryKeyboard());
  }

  @BotStateHandler(BotState.BUSINESS_INDUSTRY)
  public void processIndustry(final FundariBot fundariBot, final Message message) {
    Long chatId = message.getChatId();
    String industry = message.getText();

    BusinessApplicationForm form = businessApplicationFormService.getForm(chatId);
    form.setIndustry(industry);
    businessApplicationFormService.saveForm(chatId, form);

    botStateService.setState(chatId, BotState.BUSINESS_BUSINESS_AGE);
    fundariBot.sendMessage(chatId, "Сколько лет работает бизнес?");
  }

  @BotStateHandler(BotState.BUSINESS_BUSINESS_AGE)
  public void processBusinessAge(final FundariBot fundariBot, final Message message) {
    Long chatId = message.getChatId();
    String businessAge = message.getText();

    boolean isNumber = ValidationUtils.isNumeric(businessAge);

    if (!isNumber) {
      fundariBot.sendMessage(chatId, "Сколько лет работает бизнес? (Введите число)");
      return;
    }

    BusinessApplicationForm form = businessApplicationFormService.getForm(chatId);
    form.setBusinessAge(businessAge);
    businessApplicationFormService.saveForm(chatId, form);

    botStateService.setState(chatId, BotState.BUSINESS_AVG_MONTHLY_PROFIT);
    fundariBot.sendMessage(chatId, "Какая средняя выручка в месяц (в USD)?");
  }

  @BotStateHandler(BotState.BUSINESS_AVG_MONTHLY_PROFIT)
  public void processAvgMonthlyProfit(final FundariBot fundariBot, final Message message) {
    Long chatId = message.getChatId();
    String avgMonthlyProfit = message.getText();

    boolean isNumber = ValidationUtils.isNumeric(avgMonthlyProfit);

    if (!isNumber) {
      fundariBot.sendMessage(chatId, "Какая средняя выручка в месяц (в USD)? (Введите число)");
      return;
    }

    BigDecimal avgProfitDec = new BigDecimal(avgMonthlyProfit);

    BusinessApplicationForm form = businessApplicationFormService.getForm(chatId);
    form.setAvgMonthlyProfit(avgProfitDec);
    businessApplicationFormService.saveForm(chatId, form);

    botStateService.setState(chatId, BotState.BUSINESS_NET_PROFIT);
    fundariBot.sendMessage(chatId, "Какая чистая прибыль в месяц (в USD)?");
  }

  @BotStateHandler(BotState.BUSINESS_NET_PROFIT)
  public void processNetProfit(final FundariBot fundariBot, final Message message) {
    Long chatId = message.getChatId();
    String netProfit = message.getText();

    boolean isNumber = ValidationUtils.isNumeric(netProfit);

    if (!isNumber) {
      fundariBot.sendMessage(chatId, "Какая средняя выручка в месяц (в USD)? (Введите число)");
      return;
    }

    BigDecimal avgProfitDec = new BigDecimal(netProfit);

    BusinessApplicationForm form = businessApplicationFormService.getForm(chatId);
    form.setNetProfit(avgProfitDec);
    businessApplicationFormService.saveForm(chatId, form);

    ReplyKeyboardMarkup yesNoKeyboard = MainKeyboards.yesNoKeyboard();

    botStateService.setState(chatId, BotState.BUSINESS_HAS_ASSETS);
    fundariBot.sendMessage(
        chatId, "Есть ли у бизнеса помещения или оборудование на балансе?", yesNoKeyboard);
  }

  @BotStateHandler(BotState.BUSINESS_HAS_ASSETS)
  public void processHasAssets(final FundariBot fundariBot, final Message message) {
    Long chatId = message.getChatId();
    String answer = message.getText();

    if (!ValidationUtils.isYesOrNo(answer, fundariBot, chatId)) {
      return;
    }

    boolean hasAssets = answer.equals("Yes");

    BusinessApplicationForm form = businessApplicationFormService.getForm(chatId);
    form.setHasAssets(hasAssets);
    businessApplicationFormService.saveForm(chatId, form);

    if (hasAssets) {
      botStateService.setState(chatId, BotState.BUSINESS_ESTIMATE_VALUE_OF_ASSETS);
      fundariBot.sendMessage(chatId, "Примерная рыночная стоимость активов?");
      return;
    }

    botStateService.setState(chatId, BotState.BUSINESS_TEAM_SIZE);
    fundariBot.sendMessage(chatId, "Сколько сотрудников работает?");
  }

  @BotStateHandler(BotState.BUSINESS_ESTIMATE_VALUE_OF_ASSETS)
  public void processValueOfAssets(final FundariBot fundariBot, final Message message) {
    Long chatId = message.getChatId();
    String valueOfAssets = message.getText();

    boolean isNumber = ValidationUtils.isNumeric(valueOfAssets);

    if (!isNumber) {
      fundariBot.sendMessage(chatId, "Примерная рыночная стоимость активов? (Введите число)");
      return;
    }

    BigDecimal valueOfAssetsDec = new BigDecimal(valueOfAssets);

    BusinessApplicationForm form = businessApplicationFormService.getForm(chatId);
    form.setEstimateValueOfAssets(valueOfAssetsDec);
    businessApplicationFormService.saveForm(chatId, form);

    botStateService.setState(chatId, BotState.BUSINESS_TEAM_SIZE);
    fundariBot.sendMessage(chatId, "Сколько сотрудников работает?");
  }

  @BotStateHandler(BotState.BUSINESS_TEAM_SIZE)
  public void processTeamSize(final FundariBot fundariBot, final Message message) {
    Long chatId = message.getChatId();
    String teamSize = message.getText();

    boolean isNumber = ValidationUtils.isInteger(teamSize);

    if (!isNumber) {
      fundariBot.sendMessage(chatId, "Сколько сотрудников работает? (Введите число)");
      return;
    }

    Integer teamSizeInt = Integer.valueOf(teamSize);

    BusinessApplicationForm form = businessApplicationFormService.getForm(chatId);
    form.setTeamSize(teamSizeInt);
    businessApplicationFormService.saveForm(chatId, form);

    botStateService.setState(chatId, BotState.BUSINESS_HAS_DEBTS_OR_LOANS);
    fundariBot.sendMessage(
        chatId, "Есть ли задолженности или кредиты?", MainKeyboards.yesNoKeyboard());
  }

  @BotStateHandler(BotState.BUSINESS_HAS_DEBTS_OR_LOANS)
  public void processDebtAndLoans(final FundariBot fundariBot, final Message message) {
    Long chatId = message.getChatId();
    String answer = message.getText();

    if (!ValidationUtils.isYesOrNo(answer, fundariBot, chatId)) {
      return;
    }

    boolean hasDebts = answer.equals("Yes");

    BusinessApplicationForm form = businessApplicationFormService.getForm(chatId);
    form.setHasDebtsOrLoans(hasDebts);
    businessApplicationFormService.saveForm(chatId, form);

    ReplyKeyboardMarkup regionKeyboard = BusinessApplicationKeyboard.regionOfActivityKeyboard();

    botStateService.setState(chatId, BotState.BUSINESS_REGION_OF_ACTIVITY);
    fundariBot.sendMessage(chatId, "Где работает бизнес?", regionKeyboard);
  }

  @BotStateHandler(BotState.BUSINESS_REGION_OF_ACTIVITY)
  public void processRegionOfActivity(final FundariBot fundariBot, final Message message) {
    Long chatId = message.getChatId();
    String region = message.getText();

    BusinessApplicationForm form = businessApplicationFormService.getForm(chatId);
    form.setRegionOfActivity(region);

    ApplicationEntity application;

    // save application
    try {
      application = applicationService.saveBusinessApplication(chatId, form);
    } catch (BotException e) {
      log.error("Message: {}. ChatId: {}", e.getType().getMessage(), chatId);
      return;
    }

    // send the report
    BusinessEvalOutput aiOutput = businessEvalAiService.evaluateBusiness(application);

    String report = aiOutput.toString(form.getProjectName());

    fundariBot.sendMessage(chatId, report, MainKeyboards.responseKeyboard());

    businessApplicationFormService.clearForm(chatId);
  }
}
