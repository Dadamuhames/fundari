package com.msd.fundari.utils.telegram;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.webhook.TelegramWebhookBot;

@Slf4j
public abstract class BaseBot implements BaseBotInterface, TelegramWebhookBot {
  private final TelegramClient telegramClient;

  public BaseBot(String botToken) {
    telegramClient = new OkHttpTelegramClient(botToken);
  }

  public Message sendMessage(long chatId, String text) {
    return sendMessage(chatId, text, null);
  }

  public Message sendMessage(long chatId, String text, final ReplyKeyboard keyboardMarkup) {
    SendMessage sendMessage = new SendMessage(String.valueOf(chatId), text);
    sendMessage.setParseMode("html");
    sendMessage.setReplyMarkup(keyboardMarkup);
    return (Message) executeMethod(sendMessage);
  }

  public Object answerCallback(final String callbackId, final String message) {
    AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery(callbackId);
    answerCallbackQuery.setShowAlert(true);
    answerCallbackQuery.setText(message);
    return executeMethod(answerCallbackQuery);
  }

  public void deleteMessages(final List<Integer> messageIds, final Long chatId) {
    for (Integer messageId : messageIds) {
      DeleteMessage deleteMessage = new DeleteMessage(String.valueOf(chatId), messageId);
      deleteMessage.setMessageId(messageId);
      executeMethod(deleteMessage);
    }
  }

  public void deleteMessage(final Long chatId, final Integer messageId) {
    DeleteMessage deleteMessage = new DeleteMessage(String.valueOf(chatId), messageId);
    executeMethod(deleteMessage);
  }

  public Object executeMethod(BotApiMethod<?> method) {
    try {
      return telegramClient.execute(method);
    } catch (TelegramApiException ex) {
      log.error("Bot executeMethod error: {}", ex.getMessage());
    }

    return null;
  }

  public Object executeSendPhoto(SendPhoto sendPhoto) {
    try {
      return telegramClient.execute(sendPhoto);
    } catch (TelegramApiException ex) {
      log.error("Bot executeSendPhoto error: {}", ex.getMessage());
    }

    return null;
  }
}
