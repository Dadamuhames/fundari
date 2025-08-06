package com.msd.fundari.utils.enums;

public enum HasATeam implements LabeledEnum {
  ALONE(new String[] {"Alone", "Один(а)", "Yolgiz"}),
  TEAM(new String[] {"Has a team", "Есть команда", "Jamoa bor"});

  public final String[] labels;

  HasATeam(String[] labels) {
    this.labels = labels;
  }

  @Override
  public String[] getLabels() {
    return new String[0];
  }

  @Override
  public Enum<?>[] getValues() {
    return new Enum[0];
  }
}
