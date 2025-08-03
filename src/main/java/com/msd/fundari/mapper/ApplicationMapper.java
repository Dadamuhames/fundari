package com.msd.fundari.mapper;

import com.msd.fundari.entity.BusinessApplicationEntity;
import com.msd.fundari.entity.StartupApplicationEntity;
import com.msd.fundari.entity.redis.BusinessApplicationForm;
import com.msd.fundari.entity.redis.StartupApplicationForm;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ApplicationMapper {
  public abstract BusinessApplicationEntity formToEntity(final BusinessApplicationForm form);

  public abstract StartupApplicationEntity formToEntity(final StartupApplicationForm form);
}
