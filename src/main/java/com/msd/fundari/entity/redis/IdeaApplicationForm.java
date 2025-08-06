package com.msd.fundari.entity.redis;

import com.msd.fundari.utils.enums.IdeaTarget;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@Builder
@RedisHash("ideaApplicationForm")
public class IdeaApplicationForm {
  @Id private Long telegramId;

  private String name;

  private String description;

  private IdeaTarget target;

  private Boolean hasTeam;

  private Boolean isConceptOnly;

  private Boolean isThereSimilarProducts;

  private String difference;

  private Boolean hasInvestors;
}
