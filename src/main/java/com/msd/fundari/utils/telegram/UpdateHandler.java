package com.msd.fundari.utils.telegram;

import com.msd.fundari.utils.enums.Languages;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateHandler {
  void handleUpdate(final Update update, final BaseBotInterface bot, final Languages language);
}
