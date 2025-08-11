package com.msd.fundari.utils.enums;

public enum PrototypeOrConcept implements LabeledEnum {
  CONCEPT(new String[] {"Concept", "Концепт", "Yolgiz"}),
  PROTOTYPE(new String[] {"MVP / Прототип", "MVP / Prototype", "MVP / Prototip"});

  public final String[] labels;

  PrototypeOrConcept(String[] labels) {
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
