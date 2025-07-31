package com.msd.fundari.utils.enums;

public enum IdeaTarget implements LabeledEnum {
  INDIVIDUALS(new String[] {"Idea", "Идея", "Goya"}),
  BUSINESSES(new String[] {"Idea", "Идея", "Goya"}),
  GOVERNMENT(new String[] {"Idea", "Идея", "Goya"}),
  GLOBAL(new String[] {"Idea", "Идея", "Goya"});

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
