package com.msd.fundari.entity.redis;

import com.msd.fundari.utils.telegram.BotState;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@Builder
@RedisHash("botState")
public class BotStateEntity implements Serializable {
  @Id private Long telegramChatId;

  private BotState state;
}
