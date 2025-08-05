package com.msd.fundari.bot.keyboard;

import com.msd.fundari.service.I18nMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MainKeyboards {
  private final I18nMessageService i18nMessageService;

  public ReplyKeyboardMarkup languageKeyboard() {
    KeyboardRow rowOne = new KeyboardRow();

    rowOne.add("\uD83C\uDDEC\uD83C\uDDE7 English");
    rowOne.add("\uD83C\uDDF7\uD83C\uDDFA Русский");

    KeyboardRow rowTwo = new KeyboardRow();

    rowTwo.add("\uD83C\uDDFA\uD83C\uDDFF O'zbekcha");

    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(List.of(rowOne, rowTwo));
    replyKeyboardMarkup.setResizeKeyboard(true);

    return replyKeyboardMarkup;
  }

  public ReplyKeyboardMarkup idleKeyboard(final String lang) {
    KeyboardRow rowOne = new KeyboardRow();

    String evalBtnText = i18nMessageService.message("evalButton", lang);
    rowOne.add(String.format("\uD83E\uDDEE %s", evalBtnText));

    KeyboardRow rowTwo = new KeyboardRow();

    String contactBtnText = i18nMessageService.message("contactButton", lang);
    rowTwo.add(String.format("\uD83D\uDCE9 %s", contactBtnText));

    String changeLangText = i18nMessageService.message("changeLangButton", lang);
    rowTwo.add(String.format("\uD83C\uDF10 %s", changeLangText));

    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(List.of(rowOne, rowTwo));
    replyKeyboardMarkup.setResizeKeyboard(true);

    return replyKeyboardMarkup;
  }

  public ReplyKeyboardMarkup serviceKeyboard(final String lang) {
    KeyboardRow rowOne = new KeyboardRow();

    String runningBusiness = i18nMessageService.message("runningBusiness", lang);
    rowOne.add(String.format("\uD83E\uDDF1 %s", runningBusiness));

    KeyboardRow rowTwo = new KeyboardRow();

    String startup = i18nMessageService.message("startup", lang);
    rowTwo.add(String.format("\uD83D\uDE80 %s", startup));

    KeyboardRow rowThree = new KeyboardRow();

    String idea = i18nMessageService.message("idea", lang);
    rowThree.add(String.format("💡 %s", idea));

    ReplyKeyboardMarkup replyKeyboardMarkup =
        new ReplyKeyboardMarkup(List.of(rowOne, rowTwo, rowThree));
    replyKeyboardMarkup.setResizeKeyboard(true);
    replyKeyboardMarkup.setOneTimeKeyboard(true);

    return replyKeyboardMarkup;
  }

  public ReplyKeyboardMarkup yesNoKeyboard(final String lang) {
    KeyboardRow rowOne = new KeyboardRow();

    String yes = i18nMessageService.message("yes", lang);
    rowOne.add(yes);

    String no = i18nMessageService.message("no", lang);
    rowOne.add(no);

    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(List.of(rowOne));
    replyKeyboardMarkup.setResizeKeyboard(true);
    replyKeyboardMarkup.setOneTimeKeyboard(true);

    return replyKeyboardMarkup;
  }

  public ReplyKeyboardMarkup responseKeyboard(final String lang) {
    KeyboardRow row = new KeyboardRow();

    String evalNewProj = i18nMessageService.message("evalNewProj", lang);
    row.add(String.format("\uD83D\uDD01 %s", evalNewProj));

    String contactBtnText = i18nMessageService.message("contactButton", lang);
    row.add(String.format("\uD83D\uDCE9 %s", contactBtnText));

    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(List.of(row));
    replyKeyboardMarkup.setResizeKeyboard(true);
    replyKeyboardMarkup.setOneTimeKeyboard(true);

    return replyKeyboardMarkup;
  }

  public static ReplyKeyboardRemove replyKeyboardRemove() {
    return new ReplyKeyboardRemove(true);
  }
}
