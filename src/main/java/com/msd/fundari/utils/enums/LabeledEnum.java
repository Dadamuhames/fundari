package com.msd.fundari.utils.enums;

import java.util.Arrays;

public interface LabeledEnum {
  String[] getLabels();

  Enum<?>[] getValues();

  default LabeledEnum valueOfLabel(String label) {
    for (Enum<?> e : getValues()) {

      LabeledEnum lE = (LabeledEnum) e;

      if (Arrays.asList(((LabeledEnum) e).getLabels()).contains(label)) {
        return lE;
      }
    }
    return null;
  }
}
