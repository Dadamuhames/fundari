package com.msd.fundari.service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.msd.fundari.entity.ApplicationEntity;
import com.msd.fundari.entity.BusinessApplicationEntity;
import com.msd.fundari.entity.IdeaApplicationEntity;
import com.msd.fundari.entity.StartupApplicationEntity;
import com.msd.fundari.model.ai.output.BusinessEvalOutput;
import com.msd.fundari.model.ai.output.IdeaEvalOutput;
import com.msd.fundari.model.ai.output.StartupEvalOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoogleSheetsService {
  private final Sheets sheetsService;
  private static final String SHEET_ID = "1XRVCrn4qpgIZZo3j5JtE4ZKYzNvuJfMB9t2FiqIkD8g";

  public void writeToBusinessSheet(
      final ApplicationEntity applicationEntity, final BusinessEvalOutput evaluation)
      throws IOException {
    BusinessApplicationEntity businessApplication = applicationEntity.getBusinessApplication();

    String valueOfAssets =
        businessApplication.getEstimateValueOfAssets() != null
            ? businessApplication.getEstimateValueOfAssets().toString()
            : "No assets";

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String formattedDate = applicationEntity.getCreatedAt().format(dateFormatter);

    ValueRange body =
        new ValueRange()
            .setValues(
                List.of(
                    Arrays.asList(
                        formattedDate,
                        applicationEntity.getUser().getLanguage().toString(),
                        applicationEntity.getProjectName(),
                        businessApplication.getBusinessAge(),
                        businessApplication.getAvgMonthlyProfit(),
                        businessApplication.getNetProfit(),
                        valueOfAssets,
                        businessApplication.getTeamSize().toString(),
                        businessApplication.getRegionOfActivity(),
                        evaluation.getEstimateValue(),
                        evaluation.getMethodology())));

    sheetsService
        .spreadsheets()
        .values()
        .append(SHEET_ID, "Business!A2", body)
        .setValueInputOption("RAW")
        .setInsertDataOption("INSERT_ROWS")
        .setIncludeValuesInResponse(true)
        .execute();
  }

  public void writeToStartupSheet(
      final ApplicationEntity application, final StartupEvalOutput evaluation) throws IOException {
    StartupApplicationEntity startupApplication = application.getStartupApplicationEntity();

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String formattedDate = application.getCreatedAt().format(dateFormatter);

    String investment =
        startupApplication.getInvestedMoneyAmount() != null
            ? startupApplication.getInvestedMoneyAmount().toString()
            : "0";

    String competitors =
        startupApplication.getCompetitors() != null
            ? startupApplication.getCompetitors()
            : "No competitors";

    String lastMonthRevenue =
        startupApplication.getLastMonthRevenue() != null
            ? startupApplication.getLastMonthRevenue().toString()
            : "No revenue";

    ValueRange body =
        new ValueRange()
            .setValues(
                List.of(
                    Arrays.asList(
                        formattedDate,
                        application.getUser().getLanguage().toString(),
                        application.getProjectName(),
                        startupApplication.getStage().getLabels()[0],
                        startupApplication.getDescription(),
                        lastMonthRevenue,
                        startupApplication.getActiveUserCount().toString(),
                        investment,
                        startupApplication.getTeamSize().toString(),
                        competitors,
                        startupApplication.getRegionOfActivity(),
                        evaluation.getEstimateValue(),
                        evaluation.getMethodology())));

    sheetsService
        .spreadsheets()
        .values()
        .append(SHEET_ID, "Startup Eval!A2", body)
        .setValueInputOption("RAW")
        .setInsertDataOption("INSERT_ROWS")
        .setIncludeValuesInResponse(true)
        .execute();
  }

  public void writeToIdeaSheet(final ApplicationEntity application, final IdeaEvalOutput evaluation)
      throws IOException {
    IdeaApplicationEntity ideaApplication = application.getIdeaApplicationEntity();

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String formattedDate = application.getCreatedAt().format(dateFormatter);

    String hasATeam = ideaApplication.getHasTeam() ? "Да" : "Нет";
    String conceptOrMvp = ideaApplication.getIsConceptOnly() ? "Концепт" : "MVP";

    String difference =
        ideaApplication.getDifference() != null
            ? ideaApplication.getDifference()
            : "No Competitors";

    ValueRange body =
        new ValueRange()
            .setValues(
                List.of(
                    Arrays.asList(
                        formattedDate,
                        application.getUser().getLanguage().toString(),
                        application.getProjectName(),
                        ideaApplication.getDescription(),
                        ideaApplication.getTarget().getLabels()[0],
                        hasATeam,
                        conceptOrMvp,
                        difference,
                        evaluation.getEstimateValue(),
                        evaluation.getMethodology())));

    sheetsService
        .spreadsheets()
        .values()
        .append(SHEET_ID, "Idea Eval!A2", body)
        .setValueInputOption("RAW")
        .setInsertDataOption("INSERT_ROWS")
        .setIncludeValuesInResponse(true)
        .execute();
  }
}
