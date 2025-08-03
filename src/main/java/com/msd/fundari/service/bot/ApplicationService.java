package com.msd.fundari.service.bot;

import com.msd.fundari.entity.StartupApplicationEntity;
import com.msd.fundari.entity.redis.StartupApplicationForm;
import com.msd.fundari.mapper.ApplicationMapper;
import com.msd.fundari.entity.ApplicationEntity;
import com.msd.fundari.entity.BusinessApplicationEntity;
import com.msd.fundari.entity.UserEntity;
import com.msd.fundari.entity.redis.BusinessApplicationForm;
import com.msd.fundari.repository.ApplicationRepository;
import com.msd.fundari.repository.BusinessApplicationRepository;
import com.msd.fundari.repository.StartupApplicationRepository;
import com.msd.fundari.repository.UserRepository;
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
  private final UserRepository userRepository;
  private final ApplicationMapper applicationMapper;
  private final ApplicationRepository applicationRepository;
  private final BusinessApplicationRepository businessApplicationRepository;
  private final StartupApplicationRepository startupApplicationRepository;

  @Transactional
  public ApplicationEntity saveBusinessApplication(
      final Long chatId, final BusinessApplicationForm form) throws BotException {
    UserEntity user =
        userRepository
            .findByTelegramId(chatId)
            .orElseThrow(() -> new BotException(ExceptionType.USER_NOT_FOUND));

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
    UserEntity user =
        userRepository
            .findByTelegramId(chatId)
            .orElseThrow(() -> new BotException(ExceptionType.USER_NOT_FOUND));

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
}
