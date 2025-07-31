package com.msd.fundari.utils.enums;

public enum Languages implements LabeledEnum {
  EN(new String[] {"\uD83C\uDDEC\uD83C\uDDE7 English"}),
  RU(new String[] {"\uD83C\uDDF7\uD83C\uDDFA Русский"}),
  UZ(new String[] {"\uD83C\uDDFA\uD83C\uDDFF O'zbekcha"});

  public final String[] labels;

  Languages(String[] labels) {
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
