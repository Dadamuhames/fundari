package com.msd.fundari.bot.controller;

import com.msd.fundari.bot.FundariBot;
import com.msd.fundari.bot.keyboard.BusinessApplicationKeyboard;
import com.msd.fundari.bot.keyboard.MainKeyboards;
import com.msd.fundari.entity.ApplicationEntity;
import com.msd.fundari.entity.redis.BusinessApplicationForm;
import com.msd.fundari.model.ai.output.BusinessEvalOutput;
import com.msd.fundari.service.I18nMessageService;
import com.msd.fundari.service.ai.AiOutputSerializerService;
import com.msd.fundari.service.ai.BusinessEvalAiService;
import com.msd.fundari.service.bot.ApplicationService;
import com.msd.fundari.service.bot.redis.BotStateService;
import com.msd.fundari.service.bot.redis.BusinessApplicationFormService;
import com.msd.fundari.utils.KeyboardValidation;
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
  private final BusinessApplicationKeyboard businessApplicationKeyboard;
  private final I18nMessageService i18nMessageService;
  private final KeyboardValidation keyboardValidation;
  private final MainKeyboards mainKeyboards;
  private final AiOutputSerializerService aiOutputSerializerService;

  @BotStateHandler(BotState.BUSINESS_PROJECT_NAME)
  public void processProjectName(
      final FundariBot fundariBot, final Message message, final String lang) {
    Long chatId = message.getChatId();
    String name = message.getText();

    BusinessApplicationForm form = businessApplicationFormService.getForm(chatId);
    form.setProjectName(name);
    businessApplicationFormService.saveForm(chatId, form);

    botStateService.setState(chatId, BotState.BUSINESS_INDUSTRY);

    String enterIndustry = i18nMessageService.message("enterIndustry", lang);

    fundariBot.sendMessage(
        chatId, enterIndustry, businessApplicationKeyboard.industryKeyboard(lang));
  }

  @BotStateHandler(BotState.BUSINESS_INDUSTRY)
  public void processIndustry(
      final FundariBot fundariBot, final Message message, final String lang) {
    Long chatId = message.getChatId();
    String industry = message.getText();

    BusinessApplicationForm form = businessApplicationFormService.getForm(chatId);
    form.setIndustry(industry);
    businessApplicationFormService.saveForm(chatId, form);

    botStateService.setState(chatId, BotState.BUSINESS_BUSINESS_AGE);

    String inquireBusinessAge = i18nMessageService.message("inquireBusinessAge", lang);

    fundariBot.sendMessage(chatId, inquireBusinessAge);
  }

  @BotStateHandler(BotState.BUSINESS_BUSINESS_AGE)
  public void processBusinessAge(
      final FundariBot fundariBot, final Message message, final String lang) {
    Long chatId = message.getChatId();
    String businessAge = message.getText();

    boolean isNumber = ValidationUtils.isNumeric(businessAge);

    if (!isNumber) {
      String inquireBusinessAge = i18nMessageService.message("inquireBusinessAgeAgain", lang);
      fundariBot.sendMessage(chatId, inquireBusinessAge);
      return;
    }

    BusinessApplicationForm form = businessApplicationFormService.getForm(chatId);
    form.setBusinessAge(businessAge);
    businessApplicationFormService.saveForm(chatId, form);

    botStateService.setState(chatId, BotState.BUSINESS_AVG_MONTHLY_PROFIT);
    String inquireAvgMonthlyProfit = i18nMessageService.message("inquireAvgMonthlyProfit", lang);
    fundariBot.sendMessage(chatId, inquireAvgMonthlyProfit);
  }

  @BotStateHandler(BotState.BUSINESS_AVG_MONTHLY_PROFIT)
  public void processAvgMonthlyProfit(
      final FundariBot fundariBot, final Message message, final String lang) {
    Long chatId = message.getChatId();
    String avgMonthlyProfit = message.getText();

    boolean isNumber = ValidationUtils.isNumeric(avgMonthlyProfit);

    if (!isNumber) {
      String inquireAvgMonthlyProfitAgain =
          i18nMessageService.message("inquireAvgMonthlyProfitAgain", lang);
      fundariBot.sendMessage(chatId, inquireAvgMonthlyProfitAgain);
      return;
    }

    BigDecimal avgProfitDec = new BigDecimal(avgMonthlyProfit);

    BusinessApplicationForm form = businessApplicationFormService.getForm(chatId);
    form.setAvgMonthlyProfit(avgProfitDec);
    businessApplicationFormService.saveForm(chatId, form);

    botStateService.setState(chatId, BotState.BUSINESS_NET_PROFIT);
    String inquireNetProfit = i18nMessageService.message("inquireNetProfit", lang);
    fundariBot.sendMessage(chatId, inquireNetProfit);
  }

  @BotStateHandler(BotState.BUSINESS_NET_PROFIT)
  public void processNetProfit(
      final FundariBot fundariBot, final Message message, final String lang) {
    Long chatId = message.getChatId();
    String netProfit = message.getText();

    boolean isNumber = ValidationUtils.isNumeric(netProfit);

    if (!isNumber) {
      String inquireNetProfitAgain = i18nMessageService.message("inquireNetProfitAgain", lang);
      fundariBot.sendMessage(chatId, inquireNetProfitAgain);
      return;
    }

    BigDecimal netProfitDec = new BigDecimal(netProfit);

    BusinessApplicationForm form = businessApplicationFormService.getForm(chatId);
    form.setNetProfit(netProfitDec);
    businessApplicationFormService.saveForm(chatId, form);

    ReplyKeyboardMarkup yesNoKeyboard = mainKeyboards.yesNoKeyboard(lang);

    botStateService.setState(chatId, BotState.BUSINESS_HAS_ASSETS);
    String inquireHasAssets = i18nMessageService.message("inquireHasAssets", lang);
    fundariBot.sendMessage(chatId, inquireHasAssets, yesNoKeyboard);
  }

  @BotStateHandler(BotState.BUSINESS_HAS_ASSETS)
  public void processHasAssets(
      final FundariBot fundariBot, final Message message, final String lang) {
    Long chatId = message.getChatId();
    String answer = message.getText();

    if (!keyboardValidation.isYesOrNo(answer, lang)) {
      ReplyKeyboardMarkup yesNoKeyboard = mainKeyboards.yesNoKeyboard(lang);
      String inquireHasAssets = i18nMessageService.message("inquireHasAssets", lang);
      fundariBot.sendMessage(chatId, inquireHasAssets, yesNoKeyboard);
      return;
    }

    String localizedYes = i18nMessageService.message("yes", lang);

    boolean hasAssets = answer.equals(localizedYes);

    BusinessApplicationForm form = businessApplicationFormService.getForm(chatId);
    form.setHasAssets(hasAssets);
    businessApplicationFormService.saveForm(chatId, form);

    if (hasAssets) {
      botStateService.setState(chatId, BotState.BUSINESS_ESTIMATE_VALUE_OF_ASSETS);
      String inquireValueOfAssets = i18nMessageService.message("inquireValueOfAssets", lang);
      fundariBot.sendMessage(chatId, inquireValueOfAssets);
      return;
    }

    botStateService.setState(chatId, BotState.BUSINESS_TEAM_SIZE);
    String inquireTeamSize = i18nMessageService.message("inquireTeamSize", lang);
    fundariBot.sendMessage(chatId, inquireTeamSize);
  }

  @BotStateHandler(BotState.BUSINESS_ESTIMATE_VALUE_OF_ASSETS)
  public void processValueOfAssets(
      final FundariBot fundariBot, final Message message, final String lang) {
    Long chatId = message.getChatId();
    String valueOfAssets = message.getText();

    boolean isNumber = ValidationUtils.isNumeric(valueOfAssets);

    if (!isNumber) {
      String inquireValueOfAssetsAgain =
          i18nMessageService.message("inquireValueOfAssetsAgain", lang);
      fundariBot.sendMessage(chatId, inquireValueOfAssetsAgain);
      return;
    }

    BigDecimal valueOfAssetsDec = new BigDecimal(valueOfAssets);

    BusinessApplicationForm form = businessApplicationFormService.getForm(chatId);
    form.setEstimateValueOfAssets(valueOfAssetsDec);
    businessApplicationFormService.saveForm(chatId, form);

    botStateService.setState(chatId, BotState.BUSINESS_TEAM_SIZE);
    String inquireTeamSize = i18nMessageService.message("inquireTeamSize", lang);
    fundariBot.sendMessage(chatId, inquireTeamSize);
  }

  @BotStateHandler(BotState.BUSINESS_TEAM_SIZE)
  public void processTeamSize(
      final FundariBot fundariBot, final Message message, final String lang) {
    Long chatId = message.getChatId();
    String teamSize = message.getText();

    boolean isNumber = ValidationUtils.isInteger(teamSize);

    if (!isNumber) {
      String inquireTeamSizeAgain = i18nMessageService.message("inquireTeamSizeAgain", lang);
      fundariBot.sendMessage(chatId, inquireTeamSizeAgain);
      return;
    }

    Integer teamSizeInt = Integer.valueOf(teamSize);

    BusinessApplicationForm form = businessApplicationFormService.getForm(chatId);
    form.setTeamSize(teamSizeInt);
    businessApplicationFormService.saveForm(chatId, form);

    botStateService.setState(chatId, BotState.BUSINESS_HAS_DEBTS_OR_LOANS);
    String inquireHasDebtsOrLoans = i18nMessageService.message("inquireHasDebtsOrLoans", lang);
    fundariBot.sendMessage(chatId, inquireHasDebtsOrLoans, mainKeyboards.yesNoKeyboard(lang));
  }

  @BotStateHandler(BotState.BUSINESS_HAS_DEBTS_OR_LOANS)
  public void processDebtAndLoans(
      final FundariBot fundariBot, final Message message, final String lang) {
    Long chatId = message.getChatId();
    String answer = message.getText();

    if (!keyboardValidation.isYesOrNo(answer, lang)) {
      String inquireHasDebtsOrLoans = i18nMessageService.message("inquireHasDebtsOrLoans", lang);
      fundariBot.sendMessage(chatId, inquireHasDebtsOrLoans, mainKeyboards.yesNoKeyboard(lang));
      return;
    }

    String localizedYes = i18nMessageService.message("yes", lang);

    boolean hasDebts = answer.equals(localizedYes);

    BusinessApplicationForm form = businessApplicationFormService.getForm(chatId);
    form.setHasDebtsOrLoans(hasDebts);
    businessApplicationFormService.saveForm(chatId, form);

    ReplyKeyboardMarkup regionKeyboard = businessApplicationKeyboard.regionOfActivityKeyboard(lang);

    botStateService.setState(chatId, BotState.BUSINESS_REGION_OF_ACTIVITY);
    String inquireRegionOfActivity = i18nMessageService.message("inquireRegionOfActivity", lang);
    fundariBot.sendMessage(chatId, inquireRegionOfActivity, regionKeyboard);
  }

  @BotStateHandler(BotState.BUSINESS_REGION_OF_ACTIVITY)
  public void processRegionOfActivity(
      final FundariBot fundariBot, final Message message, final String lang) {
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

    String report =
        aiOutputSerializerService.businessEvalToString(aiOutput, form.getProjectName(), lang);

    fundariBot.sendMessage(chatId, report, mainKeyboards.responseKeyboard(lang));

    businessApplicationFormService.clearForm(chatId);
  }
}
