package com.msd.fundari.model.ai.input;

import com.msd.fundari.utils.enums.StartupStage;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record StartupEvalInput(
    StartupStage stage,
    String description,
    BigDecimal lastMonthRevenue,
    int activeUserCount,
    BigDecimal investedMoneyAmount,
    int teamSize,
    String competitors,
    String regionOfActivity,
    String language) {}
