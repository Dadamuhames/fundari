package com.msd.fundari.service.ai;

import com.msd.fundari.model.ai.output.BusinessEvalOutput;
import com.msd.fundari.model.ai.output.IdeaEvalOutput;
import com.msd.fundari.model.ai.output.StartupEvalOutput;
import com.msd.fundari.service.I18nMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiOutputSerializerService {
  private final I18nMessageService i18nMessageService;

  public String businessEvalToString(
      final BusinessEvalOutput output, final String name, final String lang) {
    String evalTemplate = i18nMessageService.message("businessEvaluationString", lang);
    String basedOnTemplate = i18nMessageService.message("businessBasedOn", lang);

    return output.toString(evalTemplate, basedOnTemplate, name);
  }

  public String startupEvalToString(final StartupEvalOutput output, final String name, final String lang) {
    String evalTemplate = i18nMessageService.message("startupEvalString", lang);
    String basedOnTemplate = i18nMessageService.message("startupBasedOn", lang);

    return output.toString(evalTemplate, basedOnTemplate, name);
  }


  public String ideaEvalToString(final IdeaEvalOutput output, final String name, final String lang) {
    String evalTemplate = i18nMessageService.message("ideaEvalString", lang);
    String basedOnTemplate = i18nMessageService.message("ideaBasedOn", lang);

    return output.toString(evalTemplate, basedOnTemplate, name);
  }
}
