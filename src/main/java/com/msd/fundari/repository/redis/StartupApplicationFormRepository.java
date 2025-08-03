package com.msd.fundari.repository.redis;

import com.msd.fundari.entity.redis.StartupApplicationForm;
import org.springframework.data.repository.CrudRepository;

public interface StartupApplicationFormRepository
    extends CrudRepository<StartupApplicationForm, Long> {}
