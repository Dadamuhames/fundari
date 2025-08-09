package com.msd.fundari.service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.msd.fundari.entity.ApplicationEntity;
import com.msd.fundari.entity.BusinessApplicationEntity;
import com.msd.fundari.entity.EvaluationReportEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoogleSheetsService {
  private final Sheets sheetsService;
  private static final String SHEET_ID = "1XRVCrn4qpgIZZo3j5JtE4ZKYzNvuJfMB9t2FiqIkD8g";

  public void writeToBusinessSheet(
      final ApplicationEntity applicationEntity, final EvaluationReportEntity evaluation)
      throws IOException {
    BusinessApplicationEntity businessApplication = applicationEntity.getBusinessApplication();

    ValueRange body =
        new ValueRange()
            .setValues(
                List.of(
                    Arrays.asList(
                        applicationEntity.getCreatedAt(),
                        applicationEntity.getUser().getLanguage(),
                        applicationEntity.getProjectName(),
                        businessApplication.getBusinessAge(),
                        businessApplication.getAvgMonthlyProfit(),
                        businessApplication.getNetProfit(),
                        businessApplication.getEstimateValueOfAssets(),
                        businessApplication.getTeamSize(),
                        businessApplication.getRegionOfActivity())));

    sheetsService
        .spreadsheets()
        .values()
        .append(
            SHEET_ID,
            "Business Eval!A1", // start at first row, column A
            body)
        .setValueInputOption("RAW")
        .setInsertDataOption("INSERT_ROWS")
        .setIncludeValuesInResponse(true)
        .execute();
  }
}
