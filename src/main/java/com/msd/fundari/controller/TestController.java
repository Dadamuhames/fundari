package com.msd.fundari.controller;

import com.msd.fundari.prompt.BusinessEvalPrompts;
import com.msd.fundari.service.GoogleSheetsService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestController {
  private final OpenAiChatModel chatModel;
  private final GoogleSheetsService googleSheetsService;

  @GetMapping("/ai/generate")
  public ChatResponse generate() {
    String messageText =
"""
{
  "industry": "Tech Startup",
  "businessAge": 2,
  "avgMonthlyProfit": 30000,
  "netProfit": 15000,
  "hasAssets": true,
  "estimateValueOfAssets": 75000,
  "teamSize": 8,
  "hasDebtsOrLoans": true,
  "regionOfActivity": "Silicon Valley",
  "language": "RU"
}

""";

    Message message = new UserMessage(messageText);

    OpenAiChatOptions options =
        OpenAiChatOptions.builder()
            .temperature(0.1)
            .topP(0.1)
            .responseFormat(ResponseFormat.builder().type(ResponseFormat.Type.JSON_OBJECT).build())
            .build();

    String systemMessageText = BusinessEvalPrompts.businessEvalSystemPrompt();

    assert systemMessageText != null;

    Message systemMessage = new SystemMessage(systemMessageText);

    Prompt prompt = new Prompt(List.of(systemMessage, message), options);

    return this.chatModel.call(prompt);
  }

  @GetMapping("/sheets")
  public ResponseEntity<?> testGoogleSheets() {
    try {
      googleSheetsService.writeToSheet();

    } catch (IOException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }

    return ResponseEntity.ok().build();
  }
}
