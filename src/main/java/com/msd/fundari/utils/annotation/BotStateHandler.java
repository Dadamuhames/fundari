package com.msd.fundari.utils.annotation;

import com.msd.fundari.utils.telegram.BotState;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BotStateHandler {
  BotState value();
}
