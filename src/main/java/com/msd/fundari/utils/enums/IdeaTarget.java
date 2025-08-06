package com.msd.fundari.utils.enums;

public enum IdeaTarget implements LabeledEnum {
  INDIVIDUALS(new String[] {"Individuals", "Физлица", "Jismoniy shaxslar"}),
  BUSINESSES(new String[] {"Businesses", "Бизнесы", "Korxonalar"}),
  GOVERNMENT(new String[] {"Government structures", "Госструктуры", "Hukumat tuzilmalari"}),
  GLOBAL(new String[] {"Global Market", "Глобальный рынок", "Jahon bozori"});

  public final String[] labels;

  IdeaTarget(String[] labels) {
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
