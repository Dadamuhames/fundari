package com.msd.fundari.config;

import com.msd.fundari.config.properties.TelegramProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramAuthFilter extends OncePerRequestFilter {
  private final TelegramProperties telegramProperties;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      @NotNull HttpServletResponse response,
      @NotNull FilterChain filterChain)
      throws ServletException, IOException {

    log.info("Telegram secret checking");

    String secretKey = request.getHeader("X-Telegram-Bot-Api-Secret-Token");

    if (secretKey == null || !secretKey.equals(telegramProperties.getSecret())) {
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      return;
    }

    filterChain.doFilter(request, response);
  }
}
