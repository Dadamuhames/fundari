package com.msd.fundari.service.bot;

import com.msd.fundari.mapper.ApplicationMapper;
import com.msd.fundari.entity.ApplicationEntity;
import com.msd.fundari.entity.BusinessApplicationEntity;
import com.msd.fundari.entity.UserEntity;
import com.msd.fundari.entity.redis.BusinessApplicationForm;
import com.msd.fundari.repository.ApplicationRepository;
import com.msd.fundari.repository.BusinessApplicationRepository;
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

  @Transactional
  public ApplicationEntity saveBusinessApplication(
      final Long chatId, final BusinessApplicationForm form) throws BotException {
    UserEntity user = userRepository.findByTelegramId(chatId).orElse(null);

    if (user == null) {
      throw new BotException(ExceptionType.USER_NOT_FOUND);
    }

    // create business application
    BusinessApplicationEntity businessApplicationEntity = applicationMapper.formToEntity(form);
    businessApplicationEntity = businessApplicationRepository.save(businessApplicationEntity);

    // create application
    ApplicationEntity applicationEntity = new ApplicationEntity();

    applicationEntity.setUser(user);
    applicationEntity.setProjectName(form.getProjectName());
    applicationEntity.setProjectType(ProjectType.BUSINESS);

    applicationEntity.setBusinessApplication(businessApplicationEntity);
    applicationRepository.save(applicationEntity);

    return applicationEntity;
  }
}
