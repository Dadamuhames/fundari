package com.msd.fundari.bot.keyboard;

import com.msd.fundari.service.I18nMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StartupApplicationKeyboard {
  private final I18nMessageService i18nMessageService;

  public ReplyKeyboardMarkup startupStageKeyboard(final String lang) {
    KeyboardRow rowOne = new KeyboardRow();
    String idea = i18nMessageService.message("idea", lang);
    rowOne.add(idea);

    KeyboardRow rowTwo = new KeyboardRow();

    rowTwo.add("MVP");

    KeyboardRow rowThree = new KeyboardRow();
    String firstClients = i18nMessageService.message("firstClients", lang);
    rowThree.add(firstClients);

    KeyboardRow rowFour = new KeyboardRow();
    String stableGrowth = i18nMessageService.message("stableGrowth", lang);
    rowFour.add(stableGrowth);

    KeyboardRow rowFive = new KeyboardRow();
    String profitBusiness = i18nMessageService.message("profitBusiness", lang);
    rowFive.add(profitBusiness);

    ReplyKeyboardMarkup replyKeyboardMarkup =
        new ReplyKeyboardMarkup(List.of(rowOne, rowTwo, rowThree, rowFour, rowFive));
    replyKeyboardMarkup.setResizeKeyboard(true);
    replyKeyboardMarkup.setOneTimeKeyboard(true);

    return replyKeyboardMarkup;
  }
}
