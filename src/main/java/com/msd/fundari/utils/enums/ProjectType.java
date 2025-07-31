package com.msd.fundari.utils.enums;

public enum ProjectType implements LabeledEnum {
  BUSINESS(new String[] {"\uD83E\uDDF1 Running business"}),
  STARTUP(new String[] {"\uD83D\uDE80 Startup"}),
  IDEA(new String[] {"\uD83D\uDCA1 Idea"});

  public final String[] labels;

  ProjectType(String[] labels) {
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
