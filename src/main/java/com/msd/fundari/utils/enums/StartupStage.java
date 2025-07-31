package com.msd.fundari.utils.enums;

public enum StartupStage implements LabeledEnum {
  IDEA(new String[] {"Idea", "Идея", "Goya"}),
  FIRST_CLIENTS(new String[] {"First clients", "Первые клиенты"}),
  MVP(new String[] {"MVP"}),
  REVENUE(new String[] {"Прибыльный бизнес"}),
  SCALING(new String[] {"Scaling", "Стабильный рост"});

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
