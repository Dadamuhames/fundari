package com.msd.fundari.entity.redis;

import com.msd.fundari.utils.enums.StartupStage;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.math.BigDecimal;

@Data
@Builder
@RedisHash("startupApplicationForm")
public class StartupApplicationForm {
  @Id private Long telegramId;

  private String projectName;

  private StartupStage stage;

  private String description;

  private BigDecimal lastMonthRevenue;

  private Integer activeUserCount;

  private BigDecimal investedMoneyAmount;

  private Integer teamSize;

  private String competitors;

  private String regionOfActivity;
}
