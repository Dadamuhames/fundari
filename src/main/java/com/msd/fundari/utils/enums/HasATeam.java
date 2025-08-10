package com.msd.fundari.utils.enums;

public enum HasATeam implements LabeledEnum {
  ALONE(new String[] {"Alone", "Один(а)", "Yolgiz"}),
  TEAM(new String[] {"Have a team", "Есть команда", "Jamoa bor"});

  public final String[] labels;

  HasATeam(String[] labels) {
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
