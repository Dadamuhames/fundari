package com.msd.fundari.utils.enums;

public enum PrototypeOrConcept implements LabeledEnum {
  CONCEPT(new String[] {"Concept", "Концепт", "Yolgiz"}),
  PROTOTYPE(new String[] {"MVP / Prototype", "MVP / Prototype", "Jamoa bor"});

  public final String[] labels;

  PrototypeOrConcept(String[] labels) {
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
