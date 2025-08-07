package com.msd.fundari.mapper;

import com.msd.fundari.entity.BusinessApplicationEntity;
import com.msd.fundari.entity.IdeaApplicationEntity;
import com.msd.fundari.entity.StartupApplicationEntity;
import com.msd.fundari.model.ai.input.BusinessEvalInput;
import com.msd.fundari.model.ai.input.IdeaEvalInput;
import com.msd.fundari.model.ai.input.StartupEvalInput;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class AiInputMapper {

  @Mapping(target = "language", source = "language")
  public abstract BusinessEvalInput businessApplicationToInput(
      final BusinessApplicationEntity entity, final String language);

  @Mapping(target = "language", source = "language")
  public abstract StartupEvalInput startupApplicationToInput(
      final StartupApplicationEntity entity, final String language);


  @Mapping(target = "language", source = "language")
  public abstract IdeaEvalInput ideaApplicationToInput(
          final IdeaApplicationEntity entity, final String language);

}
