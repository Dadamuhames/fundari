package com.msd.fundari.model.ai.output;

import lombok.Data;

@Data
public class IdeaEvalOutput {
  private Evaluation evaluation;
  private String methodology;

  public String getEstimateValue() {
    return this.evaluation.getEstimatedPotentialValue();
  }

  public String toString(
      final String templateEval, final String templateBasedOn, final String projectName) {
    String basedOnString = evaluation.getBasedOn().toString(templateBasedOn);

    return String.format(
        templateEval,
        projectName,
        evaluation.getEstimatedPotentialValue(),
        basedOnString,
        evaluation.getRecommendation(),
        methodology);
  }

  @Data
  public static class Evaluation {
    private String estimatedPotentialValue;
    private BasedOn basedOn;
    private String recommendation;
  }

  @Data
  public static class BasedOn {
    private String target;
    private String differentiation;
    private String teamStatus;
    private String developmentStage;
    private String marketCompetition;
    private String investorStatus;
    private String growthPotential;

    public String toString(final String template) {
      return String.format(
          template,
          target,
          differentiation,
          teamStatus,
          developmentStage,
          marketCompetition,
          investorStatus,
          growthPotential);
    }
  }
}
