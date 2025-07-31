package com.msd.fundari.repository;

import com.msd.fundari.entity.BusinessApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessApplicationRepository
    extends JpaRepository<BusinessApplicationEntity, Long> {}
