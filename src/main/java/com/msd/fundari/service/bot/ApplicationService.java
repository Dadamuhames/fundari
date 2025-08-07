package com.msd.fundari.service.bot;

import com.msd.fundari.entity.*;
import com.msd.fundari.entity.redis.IdeaApplicationForm;
import com.msd.fundari.entity.redis.StartupApplicationForm;
import com.msd.fundari.mapper.ApplicationMapper;
import com.msd.fundari.entity.redis.BusinessApplicationForm;
import com.msd.fundari.repository.*;
import com.msd.fundari.repository.redis.IdeaApplicationFormRepository;
import com.msd.fundari.service.UserService;
import com.msd.fundari.utils.enums.ExceptionType;
import com.msd.fundari.utils.enums.ProjectType;
import com.msd.fundari.utils.exception.BotException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationService {
  private final ApplicationMapper applicationMapper;
  private final ApplicationRepository applicationRepository;
  private final BusinessApplicationRepository businessApplicationRepository;
  private final StartupApplicationRepository startupApplicationRepository;
  private final IdeaApplicationRepository ideaApplicationRepository;
  private final UserService userService;

  @Transactional
  public ApplicationEntity saveBusinessApplication(
      final Long chatId, final BusinessApplicationForm form) throws BotException {
    UserEntity user = userService.getUser(chatId);

    // create application
    ApplicationEntity applicationEntity = new ApplicationEntity();

    applicationEntity.setUser(user);
    applicationEntity.setProjectName(form.getProjectName());
    applicationEntity.setProjectType(ProjectType.BUSINESS);
    applicationRepository.saveAndFlush(applicationEntity);

    // create business application
    BusinessApplicationEntity businessApplicationEntity = applicationMapper.formToEntity(form);
    businessApplicationEntity.setApplication(applicationEntity);
    businessApplicationEntity =
        businessApplicationRepository.saveAndFlush(businessApplicationEntity);

    applicationEntity.setBusinessApplication(businessApplicationEntity);

    applicationRepository.save(applicationEntity);

    return applicationEntity;
  }

  public ApplicationEntity saveStartUpApplication(
      final Long chatId, final StartupApplicationForm form) throws BotException {
    UserEntity user = userService.getUser(chatId);

    // crate application
    ApplicationEntity applicationEntity = new ApplicationEntity();

    applicationEntity.setUser(user);
    applicationEntity.setProjectName(form.getProjectName());
    applicationEntity.setProjectType(ProjectType.STARTUP);
    applicationEntity = applicationRepository.saveAndFlush(applicationEntity);

    // create startup application
    StartupApplicationEntity startupApplicationEntity = applicationMapper.formToEntity(form);
    startupApplicationEntity.setApplication(applicationEntity);
    startupApplicationEntity = startupApplicationRepository.saveAndFlush(startupApplicationEntity);

    applicationEntity.setStartupApplicationEntity(startupApplicationEntity);

    applicationRepository.save(applicationEntity);

    return applicationEntity;
  }

  public ApplicationEntity saveIdeaApplication(final Long chatId, final IdeaApplicationForm form)
      throws BotException {

    UserEntity user = userService.getUser(chatId);

    ApplicationEntity applicationEntity = new ApplicationEntity();

    applicationEntity.setUser(user);
    applicationEntity.setProjectName(form.getName());
    applicationEntity.setProjectType(ProjectType.IDEA);
    applicationEntity = applicationRepository.saveAndFlush(applicationEntity);

    // create idea application
    IdeaApplicationEntity ideaApplicationEntity = applicationMapper.formToEntity(form);
    ideaApplicationEntity.setApplication(applicationEntity);
    ideaApplicationEntity = ideaApplicationRepository.saveAndFlush(ideaApplicationEntity);

    applicationEntity.setIdeaApplicationEntity(ideaApplicationEntity);

    applicationRepository.save(applicationEntity);

    return applicationEntity;
  }
}
