package com.msd.fundari.bot.handler;

import com.msd.fundari.bot.FundariBot;
import com.msd.fundari.service.bot.BusinessProcessService;
import com.msd.fundari.service.bot.redis.BotStateService;
import com.msd.fundari.service.bot.MessageService;
import com.msd.fundari.utils.telegram.BaseBotInterface;
import com.msd.fundari.utils.telegram.BotState;
import com.msd.fundari.utils.telegram.UpdateHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Component
@RequiredArgsConstructor
public class MessageHandler implements UpdateHandler {
  private final BotStateService botStateService;
  private final MessageService messageService;
  private final BusinessProcessService businessProcessService;

  @Override
  public void handleUpdate(Update update, BaseBotInterface bot) {
    FundariBot fundariBot = (FundariBot) bot;

    BotState state = botStateService.getState(update.getMessage().getChatId());

    Message message = update.getMessage();

    switch (state) {
      case LANGUAGE_SELECT -> messageService.handleLanguageSelect(fundariBot, message);

      case IDLE -> messageService.handleSectionSelect(fundariBot, message);

      // business process
      case BUSINESS_PROJECT_NAME -> businessProcessService.processProjectName(fundariBot, message);

      case BUSINESS_INDUSTRY -> businessProcessService.processIndustry(fundariBot, message);

      case BUSINESS_BUSINESS_AGE -> businessProcessService.processBusinessAge(fundariBot, message);

      case BUSINESS_AVG_MONTHLY_PROFIT ->
          businessProcessService.processAvgMonthlyProfit(fundariBot, message);

      case BUSINESS_NET_PROFIT -> businessProcessService.processNetProfit(fundariBot, message);

      case BUSINESS_HAS_ASSETS -> businessProcessService.processHasAssets(fundariBot, message);

      case BUSINESS_ESTIMATE_VALUE_OF_ASSETS -> businessProcessService.processValueOfAssets(fundariBot, message);

      case BUSINESS_TEAM_SIZE -> businessProcessService.processTeamSize(fundariBot, message);

      case BUSINESS_HAS_DEBTS_OR_LOANS -> businessProcessService.processDebtAndLoans(fundariBot, message);

      case BUSINESS_REGION_OF_ACTIVITY -> businessProcessService.processRegionOfActivity(fundariBot, message);

      case null, default -> {}
    }
  }
}
