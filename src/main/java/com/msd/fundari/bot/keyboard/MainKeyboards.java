package com.msd.fundari.bot.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

public class MainKeyboards {

  public static ReplyKeyboardMarkup languageKeyboard() {
    KeyboardRow rowOne = new KeyboardRow();

    rowOne.add("\uD83C\uDDEC\uD83C\uDDE7 English");
    rowOne.add("\uD83C\uDDF7\uD83C\uDDFA Русский");

    KeyboardRow rowTwo = new KeyboardRow();

    rowTwo.add("\uD83C\uDDFA\uD83C\uDDFF O'zbekcha");

    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(List.of(rowOne, rowTwo));
    replyKeyboardMarkup.setResizeKeyboard(true);

    return replyKeyboardMarkup;
  }

  public static ReplyKeyboardMarkup idleKeyboard() {
    KeyboardRow rowOne = new KeyboardRow();

    rowOne.add("\uD83E\uDDEE Evaluate project");

    KeyboardRow rowTwo = new KeyboardRow();

    rowTwo.add("\uD83D\uDCE9 Contact Support");

    rowTwo.add("\uD83C\uDF10 Change Language");

    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(List.of(rowOne, rowTwo));
    replyKeyboardMarkup.setResizeKeyboard(true);

    return replyKeyboardMarkup;
  }

  public static ReplyKeyboardMarkup serviceKeyboard() {
    KeyboardRow rowOne = new KeyboardRow();

    rowOne.add("\uD83E\uDDF1 Running business");

    KeyboardRow rowTwo = new KeyboardRow();

    rowTwo.add("\uD83D\uDE80 Startup");

    KeyboardRow rowThree = new KeyboardRow();

    rowThree.add("💡 Idea");

    ReplyKeyboardMarkup replyKeyboardMarkup =
        new ReplyKeyboardMarkup(List.of(rowOne, rowTwo, rowThree));
    replyKeyboardMarkup.setResizeKeyboard(true);

    return replyKeyboardMarkup;
  }

  public static ReplyKeyboardMarkup yesNoKeyboard() {
    KeyboardRow rowOne = new KeyboardRow();

    rowOne.add("Yes");
    rowOne.add("No");

    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(List.of(rowOne));
    replyKeyboardMarkup.setResizeKeyboard(true);

    return replyKeyboardMarkup;
  }

  public static ReplyKeyboardMarkup responseKeyboard() {
    KeyboardRow row = new KeyboardRow();

    row.add("\uD83D\uDD01 Evaluate new project");
    row.add("\uD83D\uDCE9 Contact Support");

    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(List.of(row));
    replyKeyboardMarkup.setResizeKeyboard(true);

    return replyKeyboardMarkup;
  }
}
