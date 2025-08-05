package com.msd.fundari.utils;


import com.msd.fundari.service.I18nMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KeyboardValidation {
    private final I18nMessageService i18nMessageService;

    public boolean isYesOrNo(final String message, final String lang) {
        String yes = i18nMessageService.message("yes", lang);
        String no = i18nMessageService.message("no", lang);

        return message.equals(yes) || message.equals(no);
    }
}
