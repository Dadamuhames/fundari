package com.msd.fundari.model.ai.output;

import lombok.Data;


@Data
public class BusinessEvalOutput {
  private Evaluation evaluation;

  private String methodology;

  public String toString(final String projectName) {
    return String.format("Preliminary Valuation Report for %s:\n", projectName)
        + String.format("Estimated Value: %s\n\n", evaluation.getEstimatedValue())
        + evaluation.getBasedOn().toString()
        + String.format("Recommendation: %s\n\n", evaluation.getRecommendation())
        + String.format("Methodology: %s\n\n", methodology)
        + "*This is a preliminary estimate. Final valuation may vary with full due diligence.*";
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

  @Override
  public String toString() {
    return "\uD83E\uDDFE Based on:\n"
        + String.format("- Industry: %s\n", industry)
        + String.format("- Revenue: %s\n", revenue)
        + String.format("- Net Profit: %s\n", netProfit)
        + String.format("- Growth: %s\n", growth)
        + String.format("- Stage: %s\n", stage)
        + String.format("- Method used: %s\n", methodUsed);
  }
}
