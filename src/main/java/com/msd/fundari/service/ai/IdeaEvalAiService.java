package com.msd.fundari.service.ai;

import com.google.gson.Gson;
import com.msd.fundari.entity.ApplicationEntity;
import com.msd.fundari.mapper.AiInputMapper;
import com.msd.fundari.model.ai.input.IdeaEvalInput;
import com.msd.fundari.model.ai.input.StartupEvalInput;
import com.msd.fundari.model.ai.output.IdeaEvalOutput;
import com.msd.fundari.model.ai.output.StartupEvalOutput;
import com.msd.fundari.prompt.BusinessEvalPrompts;
import com.msd.fundari.service.EvaluationReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdeaEvalAiService {
  private final AiService aiService;
  private final AiInputMapper aiInputMapper;
  private final EvaluationReportService evaluationReportService;

  public String evaluateIdea(final String inputAsString) {
    String systemMessage = BusinessEvalPrompts.ideaEvalSystemPrompt();

    ChatResponse response = aiService.getAiResponse(inputAsString, systemMessage);

    return response.getResult().getOutput().getText();
  }

  public IdeaEvalOutput evaluateIdea(final ApplicationEntity application) {
    String language = application.getUser().getLanguage().toString();

    IdeaEvalInput input =
        aiInputMapper.ideaApplicationToInput(application.getIdeaApplicationEntity(), language);

    Gson gson = new Gson();

    String inputAsString = gson.toJson(input);

    String output = evaluateIdea(inputAsString);

    // save AI response
    evaluationReportService.save(output, application);

    return gson.fromJson(output, IdeaEvalOutput.class);
  }
}
