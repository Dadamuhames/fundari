package com.msd.fundari.bot.keyboard;

import com.msd.fundari.service.I18nMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Component
@RequiredArgsConstructor
public class IdeaApplicationKeyboard {
  private final I18nMessageService i18nMessageService;

  public ReplyKeyboardMarkup ideaTargetKeyboard(final String lang) {
    KeyboardRow rowOne = new KeyboardRow();
    String individuals = i18nMessageService.message("individuals", lang);
    rowOne.add(individuals);

    KeyboardRow rowTwo = new KeyboardRow();
    String businesses = i18nMessageService.message("businesses", lang);
    rowTwo.add(businesses);

    KeyboardRow rowThree = new KeyboardRow();
    String gov = i18nMessageService.message("gov", lang);
    rowThree.add(gov);

    KeyboardRow rowFour = new KeyboardRow();
    String global = i18nMessageService.message("global", lang);
    rowFour.add(global);

    ReplyKeyboardMarkup replyKeyboardMarkup =
        new ReplyKeyboardMarkup(List.of(rowOne, rowTwo, rowThree, rowFour));
    replyKeyboardMarkup.setResizeKeyboard(true);
    replyKeyboardMarkup.setOneTimeKeyboard(true);

    return replyKeyboardMarkup;
  }

  public ReplyKeyboardMarkup hasTeamKeyboard(final String lang) {
    KeyboardRow rowOne = new KeyboardRow();
    String alone = i18nMessageService.message("alone", lang);
    rowOne.add(alone);

    KeyboardRow rowTwo = new KeyboardRow();
    String team = i18nMessageService.message("team", lang);
    rowTwo.add(team);

    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(List.of(rowOne, rowTwo));
    replyKeyboardMarkup.setResizeKeyboard(true);
    replyKeyboardMarkup.setOneTimeKeyboard(true);

    return replyKeyboardMarkup;
  }

  public ReplyKeyboardMarkup prototypeKeyboard(final String lang) {
    KeyboardRow rowOne = new KeyboardRow();
    String concept = i18nMessageService.message("concept", lang);
    rowOne.add(concept);

    KeyboardRow rowTwo = new KeyboardRow();
    String prototype = i18nMessageService.message("prototype", lang);
    rowTwo.add(prototype);

    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(List.of(rowOne, rowTwo));
    replyKeyboardMarkup.setResizeKeyboard(true);
    replyKeyboardMarkup.setOneTimeKeyboard(true);

    return replyKeyboardMarkup;
  }
}
