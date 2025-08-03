package com.msd.fundari.service.ai;

import com.google.gson.Gson;
import com.msd.fundari.entity.ApplicationEntity;
import com.msd.fundari.mapper.AiInputMapper;
import com.msd.fundari.model.ai.input.StartupEvalInput;
import com.msd.fundari.model.ai.output.StartupEvalOutput;
import com.msd.fundari.prompt.BusinessEvalPrompts;
import com.msd.fundari.service.EvaluationReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StartupEvalAiService {
  private final AiService aiService;
  private final AiInputMapper aiInputMapper;
  private final EvaluationReportService evaluationReportService;

  public String evaluateStartup(final String inputAsString) {
    String systemMessage = BusinessEvalPrompts.startupEvalSystemPrompt();

    ChatResponse response = aiService.getAiResponse(inputAsString, systemMessage);

    return response.getResult().getOutput().getText();
  }

  public StartupEvalOutput evaluateStartup(final ApplicationEntity application) {
    String language = application.getUser().getLanguage().toString();

    StartupEvalInput input =
        aiInputMapper.startupApplicationToInput(
            application.getStartupApplicationEntity(), language);

    Gson gson = new Gson();

    String inputAsString = gson.toJson(input);

    String output = evaluateStartup(inputAsString);

    // save AI response
    evaluationReportService.save(output, application);

    return gson.fromJson(output, StartupEvalOutput.class);
  }
}
