package com.msd.fundari.model.ai.output;

import lombok.Data;

@Data
public class BusinessEvalOutput {
  private Evaluation evaluation;

  private String methodology;

  public String getEstimateValue() {
    return this.evaluation.getEstimatedValue();
  }

  public String toString(
      final String templateEval, final String templateBasedOn, final String projectName) {
    String basedOnString = evaluation.getBasedOn().toString(templateBasedOn);

    return String.format(
        templateEval,
        projectName,
        evaluation.getEstimatedValue(),
        basedOnString,
        evaluation.getRecommendation(),
        methodology);
  }
}

@Data
class Evaluation {
  private String estimatedValue;
  private BasedOn basedOn;
  private String recommendation;
}

@Data
class BasedOn {
  private String industry;
  private String revenue;
  private String netProfit;
  private String growth;
  private String stage;
  private String methodUsed;

  public String toString(final String template) {
    return String.format(template, industry, revenue, netProfit, growth, stage, methodUsed);
  }
}
