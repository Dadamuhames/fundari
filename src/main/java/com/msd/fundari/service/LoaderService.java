package com.msd.fundari.service;

import com.msd.fundari.bot.FundariBot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
public class LoaderService {
  private final I18nMessageService i18nMessageService;

  public void loader(
      final Future<?> futureTask, final FundariBot fundariBot, final Long chatId, final String lang)
      throws InterruptedException {
    String loadingText = i18nMessageService.message("loaderText", lang);

    Message loaderMessage = null;
    int dotCount = 1;

    while (!futureTask.isDone()) {
      if (loaderMessage == null) {
        loaderMessage = fundariBot.sendMessage(chatId, loadingText + ".");
      } else {
        if (dotCount == 3) {
          dotCount = 1;
        } else {
          dotCount++;
        }

        String loadingMessage = loadingText + ".".repeat(dotCount);
        EditMessageText editMessageText = new EditMessageText(loadingMessage);
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(loaderMessage.getMessageId());
        fundariBot.executeMethod(editMessageText);
      }

      Thread.sleep(100);
    }

    assert loaderMessage != null;
    fundariBot.deleteMessage(chatId, loaderMessage.getMessageId());
  }
}
