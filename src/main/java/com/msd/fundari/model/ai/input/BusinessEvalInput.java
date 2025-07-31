package com.msd.fundari.model.ai.input;

import lombok.Builder;

@Builder
public record BusinessEvalInput(
    String industry,
    int businessAge,
    Double avgMonthlyProfit,
    Double netProfit,
    boolean hasAssets,
    Double estimateValueOfAssets,
    int teamSize,
    boolean hasDebtsOrLoans,
    String regionOfActivity,
    String language) {}
