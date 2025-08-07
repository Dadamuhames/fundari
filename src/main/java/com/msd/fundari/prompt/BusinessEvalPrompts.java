package com.msd.fundari.prompt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Slf4j
public class BusinessEvalPrompts {
  public static String readFile(final String filePath) {
    try {
      File file = ResourceUtils.getFile(String.format("classpath:prompts/%s", filePath));
      return Files.readString(file.toPath(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      log.error("Prompt File not found: {}", e.getMessage());
    }

    return null;
  }

  public static String businessEvalSystemPrompt() {
    return readFile("businessEvaluation.txt");
  }

  public static String startupEvalSystemPrompt() {
    return readFile("startupEvaluation.txt");
  }

  public static String ideaEvalSystemPrompt() {
    return readFile("ideaEvaluation.txt");
  }
}
