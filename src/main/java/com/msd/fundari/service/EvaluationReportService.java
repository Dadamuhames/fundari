package com.msd.fundari.service;

import com.msd.fundari.entity.ApplicationEntity;
import com.msd.fundari.entity.EvaluationReportEntity;
import com.msd.fundari.repository.EvaluationReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EvaluationReportService {
  private final EvaluationReportRepository evaluationReportRepository;

  public void save(final String responseJson, final ApplicationEntity application) {
    EvaluationReportEntity entity =
        EvaluationReportEntity.builder().application(application).reportJson(responseJson).build();

    evaluationReportRepository.save(entity);
  }
}
