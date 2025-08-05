package com.msd.fundari.bot.keyboard;

import com.msd.fundari.service.I18nMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BusinessApplicationKeyboard {
  private final I18nMessageService i18nMessageService;

  public ReplyKeyboardMarkup industryKeyboard(final String lang) {
    KeyboardRow rowOne = new KeyboardRow();
    String trade = i18nMessageService.message("trade", lang);
    rowOne.add(trade);

    KeyboardRow rowTwo = new KeyboardRow();
    String service = i18nMessageService.message("service", lang);
    rowTwo.add(service);

    KeyboardRow rowThree = new KeyboardRow();
    String food = i18nMessageService.message("foodService", lang);
    rowThree.add(food);

    KeyboardRow rowFour = new KeyboardRow();
    String manufacture = i18nMessageService.message("manufacture", lang);
    rowFour.add(manufacture);

    ReplyKeyboardMarkup replyKeyboardMarkup =
        new ReplyKeyboardMarkup(List.of(rowOne, rowTwo, rowThree, rowFour));
    replyKeyboardMarkup.setResizeKeyboard(true);
    replyKeyboardMarkup.setOneTimeKeyboard(true);

    return replyKeyboardMarkup;
  }

  public ReplyKeyboardMarkup regionOfActivityKeyboard(final String lang) {
    KeyboardRow rowOne = new KeyboardRow();
    String tashkent = i18nMessageService.message("tashkent", lang);
    rowOne.add(tashkent);

    KeyboardRow rowTwo = new KeyboardRow();
    String regions = i18nMessageService.message("regions", lang);
    rowTwo.add(regions);

    KeyboardRow rowThree = new KeyboardRow();
    String internationalMarket = i18nMessageService.message("internationalMarket", lang);
    rowThree.add(internationalMarket);

    ReplyKeyboardMarkup replyKeyboardMarkup =
        new ReplyKeyboardMarkup(List.of(rowOne, rowTwo, rowThree));
    replyKeyboardMarkup.setResizeKeyboard(true);
    replyKeyboardMarkup.setOneTimeKeyboard(true);

    return replyKeyboardMarkup;
  }
}
