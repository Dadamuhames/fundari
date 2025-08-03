package com.msd.fundari.bot.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

public class StartupApplicationKeyboard {

  public static ReplyKeyboardMarkup startupStageKeyboard() {
    KeyboardRow rowOne = new KeyboardRow();

    rowOne.add("Идея");

    KeyboardRow rowTwo = new KeyboardRow();

    rowTwo.add("MVP");

    KeyboardRow rowThree = new KeyboardRow();

    rowThree.add("Первые клиенты");

    KeyboardRow rowFour = new KeyboardRow();

    rowFour.add("Стабильный рост");

    KeyboardRow rowFive = new KeyboardRow();

    rowFive.add("Прибыльный бизнес");

    ReplyKeyboardMarkup replyKeyboardMarkup =
        new ReplyKeyboardMarkup(List.of(rowOne, rowTwo, rowThree, rowFour, rowFive));
    replyKeyboardMarkup.setResizeKeyboard(true);
    replyKeyboardMarkup.setOneTimeKeyboard(true);

    return replyKeyboardMarkup;
  }
}
