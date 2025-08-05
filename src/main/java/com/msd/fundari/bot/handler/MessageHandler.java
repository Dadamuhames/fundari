package com.msd.fundari.bot.handler;

import com.msd.fundari.bot.FundariBot;
import com.msd.fundari.service.bot.redis.BotStateService;
import com.msd.fundari.utils.HelperMethods;
import com.msd.fundari.utils.enums.Languages;
import com.msd.fundari.utils.telegram.BaseBotInterface;
import com.msd.fundari.utils.telegram.BotState;
import com.msd.fundari.utils.telegram.UpdateHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.lang.reflect.Method;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageHandler implements UpdateHandler {
  private final BotStateService botStateService;
  private final Map<BotState, Method> botStateHandlerMap;
  private final ApplicationContext ctx;

  @Override
  public void handleUpdate(
      final Update update, final BaseBotInterface bot, final Languages language) {
    FundariBot fundariBot = (FundariBot) bot;

    BotState state = botStateService.getState(update.getMessage().getChatId());

    Message message = update.getMessage();

    Method handler = botStateHandlerMap.get(state);

    if (handler != null) {
      try {
        String className = handler.getDeclaringClass().getSimpleName();
        String beanName = HelperMethods.toCamelCase(className);
        Object handlerClass = ctx.getBean(beanName);

        handler.invoke(handlerClass, fundariBot, message, language.toString().toLowerCase());
      } catch (Exception e) {
        log.error("Handler invoke error: {}", e.getMessage());
      }
    }
  }
}
