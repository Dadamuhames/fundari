package com.msd.fundari.service.ai;

import com.google.gson.Gson;
import com.msd.fundari.entity.ApplicationEntity;
import com.msd.fundari.mapper.AiInputMapper;
import com.msd.fundari.model.ai.input.BusinessEvalInput;
import com.msd.fundari.model.ai.output.BusinessEvalOutput;
import com.msd.fundari.prompt.BusinessEvalPrompts;
import com.msd.fundari.service.EvaluationReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BusinessEvalAiService {
  private final AiService aiService;
  private final AiInputMapper aiInputMapper;
  private final EvaluationReportService evaluationReportService;

  public String evaluateBusiness(final String inputAsString) {
    String systemMessage = BusinessEvalPrompts.businessEvalSystemPrompt();

    ChatResponse response = aiService.getAiResponse(inputAsString, systemMessage);

    return response.getResult().getOutput().getText();
  }

  public BusinessEvalOutput evaluateBusiness(final ApplicationEntity application) {
    String language = application.getUser().getLanguage().toString();

    BusinessEvalInput input =
        aiInputMapper.businessApplicationToInput(application.getBusinessApplication(), language);

    Gson gson = new Gson();

    String inputAsString = gson.toJson(input);

    String output = evaluateBusiness(inputAsString);

    // save AI response
    evaluationReportService.save(output, application);

    return gson.fromJson(output, BusinessEvalOutput.class);
  }
}
