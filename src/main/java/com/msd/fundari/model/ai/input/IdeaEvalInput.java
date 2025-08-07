package com.msd.fundari.model.ai.input;

public record IdeaEvalInput(
    String description,
    String target,
    boolean hasTeam,
    boolean isConceptOnly,
    boolean isThereSimilarProducts,
    String difference,
    boolean hasInvestors,
    String language) {}
