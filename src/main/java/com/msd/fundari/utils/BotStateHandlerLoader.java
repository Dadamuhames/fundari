package com.msd.fundari.utils;

import com.msd.fundari.utils.annotation.BotStateController;
import com.msd.fundari.utils.annotation.BotStateHandler;
import com.msd.fundari.utils.telegram.BaseBot;
import com.msd.fundari.utils.telegram.BotState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BotStateHandlerLoader {
  @Bean(name = "botStateHandlerMap")
  public Map<BotState, Method> load() {
    Map<BotState, Method> handlersMap = new HashMap<>();

    ClassPathScanningCandidateComponentProvider scanner =
        new ClassPathScanningCandidateComponentProvider(false);

    // Add filter for classes with @MyAnnotation
    scanner.addIncludeFilter(new AnnotationTypeFilter(BotStateController.class));

    // Specify the base package to scan (replace with your package)
    String basePackage = "com.msd.fundari";
    Set<BeanDefinition> beanDefinitions = scanner.findCandidateComponents(basePackage);

    // Use the context classloader to avoid classloader mismatches
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    for (BeanDefinition beanDefinition : beanDefinitions) {
      try {
        String className = beanDefinition.getBeanClassName();
        Class<?> clazz = Class.forName(className, true, classLoader);

        for (Method method : clazz.getDeclaredMethods()) {
          if (method.isAnnotationPresent(BotStateHandler.class)) {
            BotState state = method.getAnnotation(BotStateHandler.class).value();

            handlersMap.put(state, method);
          }
        }

      } catch (Exception e) {
        log.error("Handle loading error: {}", e.getMessage());
      }
    }

    return handlersMap;
  }
}
