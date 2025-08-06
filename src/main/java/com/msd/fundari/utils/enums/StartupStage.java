package com.msd.fundari.utils.enums;

public enum StartupStage implements LabeledEnum {
  IDEA(new String[] {"Idea", "Идея", "Goya"}),
  FIRST_CLIENTS(new String[] {"First clients", "Первые клиенты", "Birinchi mijozlar"}),
  MVP(new String[] {"MVP"}),
  REVENUE(new String[] {"Прибыльный бизнес", "Profitable business", "Foydali biznes"}),
  SCALING(new String[] {"Scaling", "Стабильный рост", "Barqaror o'sish"});

  public final String[] labels;

  StartupStage(String[] labels) {
    this.labels = labels;
  }

  @Override
  public String[] getLabels() {
    return this.labels;
  }

  @Override
  public Enum<?>[] getValues() {
    return values();
  }
}
