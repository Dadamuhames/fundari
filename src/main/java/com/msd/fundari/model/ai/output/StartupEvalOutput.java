package com.msd.fundari.model.ai.output;

import lombok.Data;

@Data
public class StartupEvalOutput {
  private Evaluation evaluation;
  private String methodology;

  public String toString(String projectName) {
    return String.format("Preliminary Valuation Report for %s:\n", projectName)
        + String.format("Estimated Value: %s\n\n", evaluation.getEstimatedValue())
        + evaluation.getBasedOn().toString()
        + String.format("Recommendation: %s\n\n", evaluation.getRecommendation())
        + String.format("Methodology: %s\n\n", methodology)
        + "*This is a preliminary estimate. Final valuation may vary with full due diligence.*";
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

    @Override
    public String toString() {
      return "\uD83E\uDDFE Based on:\n"
          + String.format("- Stage of startup: %s\n", stage)
          + String.format("- Revenue: %s\n", revenue)
          + String.format("- Active users: %s\n", activeUsers)
          + String.format("- Growth: %s\n", growth)
          + String.format("- Stage: %s\n", stage) // Repeated as per original
          + String.format("- Method used: %s\n", methodUsed);
    }
  }
}
