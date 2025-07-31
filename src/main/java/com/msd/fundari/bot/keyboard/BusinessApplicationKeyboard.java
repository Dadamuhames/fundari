package com.msd.fundari.bot.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

public class BusinessApplicationKeyboard {
  public static ReplyKeyboardMarkup industryKeyboard() {
    KeyboardRow rowOne = new KeyboardRow();

    rowOne.add("Торговля");

    KeyboardRow rowTwo = new KeyboardRow();

    rowTwo.add("Услуги");

    KeyboardRow rowThree = new KeyboardRow();

    rowThree.add("Общепит");

    KeyboardRow rowFour = new KeyboardRow();

    rowFour.add("Производство");

    ReplyKeyboardMarkup replyKeyboardMarkup =
        new ReplyKeyboardMarkup(List.of(rowOne, rowTwo, rowThree, rowFour));
    replyKeyboardMarkup.setResizeKeyboard(true);

    return replyKeyboardMarkup;
  }

  public static ReplyKeyboardMarkup regionOfActivityKeyboard() {
    KeyboardRow rowOne = new KeyboardRow();

    rowOne.add("Ташкент");

    KeyboardRow rowTwo = new KeyboardRow();

    rowTwo.add("Регионы");

    KeyboardRow rowThree = new KeyboardRow();

    rowThree.add("Международный рынок");

    ReplyKeyboardMarkup replyKeyboardMarkup =
        new ReplyKeyboardMarkup(List.of(rowOne, rowTwo, rowThree));
    replyKeyboardMarkup.setResizeKeyboard(true);

    return replyKeyboardMarkup;
  }
}
