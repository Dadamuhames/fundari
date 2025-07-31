package com.msd.fundari.service.ai;

import com.google.gson.Gson;
import com.msd.fundari.model.ai.input.BusinessEvalInput;
import com.msd.fundari.model.ai.output.BusinessEvalOutput;
import com.msd.fundari.prompt.BusinessEvalPrompts;
import com.msd.fundari.service.EvaluationReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AiService {
  private final OpenAiChatModel chatModel;
  private final EvaluationReportService evaluationReportService;

  public ChatResponse getAiResponse(final String input, final String systemPrompt) {
    Message message = new UserMessage(input);

    OpenAiChatOptions options =
        OpenAiChatOptions.builder()
            .temperature(0.1)
            .topP(0.1)
            .responseFormat(ResponseFormat.builder().type(ResponseFormat.Type.JSON_OBJECT).build())
            .build();

    Message systemMessage = new SystemMessage(systemPrompt);

    Prompt prompt = new Prompt(List.of(systemMessage, message), options);

    ChatResponse chatResponse = chatModel.call(prompt);

    return chatResponse;
  }
}
