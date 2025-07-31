package com.msd.fundari.mapper;

import com.msd.fundari.entity.BusinessApplicationEntity;
import com.msd.fundari.entity.redis.BusinessApplicationForm;
import com.msd.fundari.model.ai.input.BusinessEvalInput;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class AiInputMapper {

  @Mapping(target = "language", source = "language")
  public abstract BusinessEvalInput businessApplicationToInput(
      final BusinessApplicationEntity entity, final String language);
}
