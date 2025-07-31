package com.msd.fundari.entity.redis;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.math.BigDecimal;

@Data
@Builder
@RedisHash("businessApplicationForm")
public class BusinessApplicationForm {
  @Id
  private Long telegramId;

  private String projectName;

  private String industry;

  private String businessAge;

  private BigDecimal avgMonthlyProfit;

  private BigDecimal netProfit;

  private BigDecimal estimateValueOfAssets;

  private Integer teamSize;

  private boolean hasAssets;

  private boolean hasDebtsOrLoans;

  private String regionOfActivity;
}
