package com.msd.fundari.model.ai.output;

public record BusinessEvalOutput(Evaluation evaluation, String methodology) {

  public String toString(final String projectName) {
    return String.format("Preliminary Valuation Report for %s:\n", projectName)
        + String.format("Estimated Value: %s\n\n", evaluation.estimatedValue())
        + evaluation.basedOn().toString()
        + String.format("Recommendation: %s\n\n", evaluation.recommendation())
        + String.format("Methodology: %s\n\n", methodology)
        + "*This is a preliminary estimate. Final valuation may vary with full due diligence.*";
  }
}

record Evaluation(String estimatedValue, BasedOn basedOn, String recommendation) {}

record BasedOn(
    String industry,
    String revenue,
    String netProfit,
    String growth,
    String stage,
    String methodUsed) {

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
