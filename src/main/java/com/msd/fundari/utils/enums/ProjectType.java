package com.msd.fundari.utils.enums;

public enum ProjectType implements LabeledEnum {
  BUSINESS(
      new String[] {
        "\uD83E\uDDF1 Running business",
        "\uD83E\uDDF1 Действующий бизнес",
        "\uD83E\uDDF1 Faol biznes"
      }),
  STARTUP(new String[] {"\uD83D\uDE80 Startup", "\uD83D\uDE80 Стартап", "\uD83D\uDE80 Startap"}),
  IDEA(new String[] {"\uD83D\uDCA1 Idea", "\uD83D\uDCA1 Идея", "\uD83D\uDCA1 G'oya"});

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
