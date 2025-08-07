package com.msd.fundari.bot.controller;

import com.msd.fundari.bot.FundariBot;
import com.msd.fundari.bot.keyboard.IdeaApplicationKeyboard;
import com.msd.fundari.bot.keyboard.MainKeyboards;
import com.msd.fundari.entity.ApplicationEntity;
import com.msd.fundari.entity.redis.IdeaApplicationForm;
import com.msd.fundari.model.ai.output.IdeaEvalOutput;
import com.msd.fundari.model.ai.output.StartupEvalOutput;
import com.msd.fundari.repository.redis.IdeaApplicationFormRepository;
import com.msd.fundari.service.I18nMessageService;
import com.msd.fundari.service.ai.AiOutputSerializerService;
import com.msd.fundari.service.ai.IdeaEvalAiService;
import com.msd.fundari.service.bot.ApplicationService;
import com.msd.fundari.service.bot.redis.BotStateService;
import com.msd.fundari.service.bot.redis.IdeaApplicationFormService;
import com.msd.fundari.utils.KeyboardValidation;
import com.msd.fundari.utils.annotation.BotStateController;
import com.msd.fundari.utils.annotation.BotStateHandler;
import com.msd.fundari.utils.enums.HasATeam;
import com.msd.fundari.utils.enums.IdeaTarget;
import com.msd.fundari.utils.enums.PrototypeOrConcept;
import com.msd.fundari.utils.exception.BotException;
import com.msd.fundari.utils.telegram.BotState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Slf4j
@Component
@BotStateController
@RequiredArgsConstructor
public class IdeaStateController {
  private final IdeaApplicationFormService ideaApplicationFormService;
  private final BotStateService botStateService;
  private final ApplicationService applicationService;
  private final MainKeyboards mainKeyboards;
  private final KeyboardValidation keyboardValidation;
  private final I18nMessageService i18nMessageService;
  private final IdeaApplicationKeyboard ideaApplicationKeyboard;
  private final IdeaEvalAiService ideaEvalAiService;
  private final AiOutputSerializerService aiOutputSerializerService;

  @BotStateHandler(BotState.IDEA_PROJECT_NAME)
  public void processIdeaName(
      final FundariBot fundariBot, final Message message, final String lang) {
    Long chatId = message.getChatId();
    String name = message.getText();

    IdeaApplicationForm form = ideaApplicationFormService.getForm(chatId);
    form.setName(name);
    ideaApplicationFormService.saveForm(chatId, form);

    botStateService.setState(chatId, BotState.IDEA_DESCRIPTION);
    String inquireIdeaDescription = i18nMessageService.message("inquireIdeaDescription", lang);
    fundariBot.sendMessage(chatId, inquireIdeaDescription, MainKeyboards.replyKeyboardRemove());
  }

  @BotStateHandler(BotState.IDEA_DESCRIPTION)
  public void processIdeaDescription(
      final FundariBot fundariBot, final Message message, final String lang) {
    Long chatId = message.getChatId();
    String desc = message.getText();

    IdeaApplicationForm form = ideaApplicationFormService.getForm(chatId);
    form.setDescription(desc);
    ideaApplicationFormService.saveForm(chatId, form);

    botStateService.setState(chatId, BotState.IDEA_TARGET);
    String inquireIdeaTarget = i18nMessageService.message("inquireIdeaTarget", lang);
    fundariBot.sendMessage(
        chatId, inquireIdeaTarget, ideaApplicationKeyboard.ideaTargetKeyboard(lang));
  }

  @BotStateHandler(BotState.IDEA_TARGET)
  public void processIdeaTarget(
      final FundariBot fundariBot, final Message message, final String lang) {
    Long chatId = message.getChatId();

    IdeaTarget ideaTarget = (IdeaTarget) IdeaTarget.GLOBAL.valueOfLabel(message.getText());

    if (ideaTarget == null) {
      String inquireIdeaTarget = i18nMessageService.message("inquireIdeaTarget", lang);
      fundariBot.sendMessage(
          chatId, inquireIdeaTarget, ideaApplicationKeyboard.ideaTargetKeyboard(lang));
      return;
    }

    IdeaApplicationForm form = ideaApplicationFormService.getForm(chatId);
    form.setTarget(ideaTarget);
    ideaApplicationFormService.saveForm(chatId, form);

    botStateService.setState(chatId, BotState.IDEA_HAS_A_TEAM);
    String inquireTeam = i18nMessageService.message("inquireTeam", lang);
    fundariBot.sendMessage(chatId, inquireTeam, ideaApplicationKeyboard.hasTeamKeyboard(lang));
  }

  @BotStateHandler(BotState.IDEA_HAS_A_TEAM)
  public void processHasTeam(
      final FundariBot fundariBot, final Message message, final String lang) {
    Long chatId = message.getChatId();

    HasATeam hasATeam = (HasATeam) HasATeam.TEAM.valueOfLabel(message.getText());

    if (hasATeam == null) {
      String inquireTeam = i18nMessageService.message("inquireTeam", lang);
      fundariBot.sendMessage(chatId, inquireTeam, ideaApplicationKeyboard.hasTeamKeyboard(lang));
      return;
    }

    IdeaApplicationForm form = ideaApplicationFormService.getForm(chatId);
    form.setHasTeam(hasATeam.equals(HasATeam.TEAM));
    ideaApplicationFormService.saveForm(chatId, form);

    botStateService.setState(chatId, BotState.IDEA_IS_PROTOTYPE_ONLY);
    String isPrototype = i18nMessageService.message("isPrototype", lang);
    fundariBot.sendMessage(chatId, isPrototype, ideaApplicationKeyboard.prototypeKeyboard(lang));
  }

  @BotStateHandler(BotState.IDEA_IS_PROTOTYPE_ONLY)
  public void processIsPrototypeOnly(
      final FundariBot fundariBot, final Message message, final String lang) {
    Long chatId = message.getChatId();

    PrototypeOrConcept prototypeOrConcept =
        (PrototypeOrConcept) PrototypeOrConcept.PROTOTYPE.valueOfLabel(message.getText());

    if (prototypeOrConcept == null) {
      String isPrototype = i18nMessageService.message("isPrototype", lang);
      fundariBot.sendMessage(chatId, isPrototype, ideaApplicationKeyboard.prototypeKeyboard(lang));
      return;
    }

    IdeaApplicationForm form = ideaApplicationFormService.getForm(chatId);
    form.setIsConceptOnly(prototypeOrConcept.equals(PrototypeOrConcept.CONCEPT));
    ideaApplicationFormService.saveForm(chatId, form);

    botStateService.setState(chatId, BotState.IDEA_IS_THERE_SIMILAR_PRODUCT);
    String similarIdea = i18nMessageService.message("similarIdea", lang);
    fundariBot.sendMessage(chatId, similarIdea, mainKeyboards.yesNoKeyboard(lang));
  }

  @BotStateHandler(BotState.IDEA_IS_THERE_SIMILAR_PRODUCT)
  public void processSimilarProduct(
      final FundariBot fundariBot, final Message message, final String lang) {
    Long chatId = message.getChatId();
    String answer = message.getText();

    if (!keyboardValidation.isYesOrNo(answer, lang)) {
      String similarIdeaAgain = i18nMessageService.message("similarIdeaAgain", lang);
      fundariBot.sendMessage(chatId, similarIdeaAgain, mainKeyboards.yesNoKeyboard(lang));
      return;
    }

    String yesLocalized = i18nMessageService.message("yes", lang);
    boolean isYes = yesLocalized.equals(answer);

    IdeaApplicationForm form = ideaApplicationFormService.getForm(chatId);
    form.setIsThereSimilarProducts(isYes);
    ideaApplicationFormService.saveForm(chatId, form);

    if (isYes) {
      botStateService.setState(chatId, BotState.IDEA_DIFFERENCE);
      String ideaDifference = i18nMessageService.message("ideaDifference", lang);
      fundariBot.sendMessage(chatId, ideaDifference);
      return;
    }

    botStateService.setState(chatId, BotState.IDEA_HAS_INVESTORS);
    String hasInvestors = i18nMessageService.message("hasInvestors", lang);
    fundariBot.sendMessage(chatId, hasInvestors, mainKeyboards.yesNoKeyboard(lang));
  }

  @BotStateHandler(BotState.IDEA_DIFFERENCE)
  public void processDifference(
      final FundariBot fundariBot, final Message message, final String lang) {

    Long chatId = message.getChatId();

    String difference = message.getText();

    IdeaApplicationForm form = ideaApplicationFormService.getForm(chatId);
    form.setDifference(difference);
    ideaApplicationFormService.saveForm(chatId, form);

    botStateService.setState(chatId, BotState.IDEA_HAS_INVESTORS);
    String hasInvestors = i18nMessageService.message("hasInvestors", lang);
    fundariBot.sendMessage(chatId, hasInvestors, mainKeyboards.yesNoKeyboard(lang));
  }

  @BotStateHandler(BotState.IDEA_HAS_INVESTORS)
  public void processHasInvestors(
      final FundariBot fundariBot, final Message message, final String lang) {
    Long chatId = message.getChatId();
    String answer = message.getText();

    if (!keyboardValidation.isYesOrNo(answer, lang)) {
      String hasInvestors = i18nMessageService.message("hasInvestors", lang);
      fundariBot.sendMessage(chatId, hasInvestors, mainKeyboards.yesNoKeyboard(lang));
      return;
    }

    String yesLocalized = i18nMessageService.message("yes", lang);
    boolean hasInvestors = yesLocalized.equals(answer);
    IdeaApplicationForm form = ideaApplicationFormService.getForm(chatId);
    form.setHasInvestors(hasInvestors);

    ApplicationEntity application;

    // save application
    try {
      application = applicationService.saveIdeaApplication(chatId, form);
    } catch (BotException e) {
      log.error("Message: {}. ChatId: {}", e.getType().getMessage(), chatId);
      return;
    }

    // send the report
    IdeaEvalOutput aiOutput = ideaEvalAiService.evaluateIdea(application);

    String report = aiOutputSerializerService.ideaEvalToString(aiOutput, form.getName(), lang);

    fundariBot.sendMessage(chatId, report, mainKeyboards.responseKeyboard(lang));

    ideaApplicationFormService.clearForm(chatId);
  }
}
