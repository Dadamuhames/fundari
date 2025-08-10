package com.msd.fundari.model.ai.output;

import lombok.Data;

@Data
public class StartupEvalOutput {
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

  @Data
  public static class Evaluation {
    private String estimatedValue;
    private BasedOn basedOn;
    private String recommendation;
  }

  @Data
  public static class BasedOn {
    private String stage;
    private String revenue;
    private String activeUsers;
    private String investment;
    private String growth;
    private String methodUsed;

    public String toString(final String template) {
      return String.format(template, stage, revenue, activeUsers, growth, methodUsed);
    }
  }
}
