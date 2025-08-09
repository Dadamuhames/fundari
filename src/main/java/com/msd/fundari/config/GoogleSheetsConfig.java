package com.msd.fundari.config;

import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.SheetsScopes;
import java.io.IOException;
import java.security.GeneralSecurityException;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Objects;

@Configuration
public class GoogleSheetsConfig {

  private static final String APPLICATION_NAME = "Spring Boot Google Sheets";
  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
  private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

  @Bean
  public Sheets sheetsService() throws IOException, GeneralSecurityException {
    GoogleCredentials credentials =
        GoogleCredentials.fromStream(
                Objects.requireNonNull(getClass().getResourceAsStream(CREDENTIALS_FILE_PATH)))
            .createScoped(SheetsScopes.SPREADSHEETS);

    return new Sheets.Builder(
            GoogleNetHttpTransport.newTrustedTransport(),
            JSON_FACTORY,
            new HttpCredentialsAdapter(credentials))
        .setApplicationName(APPLICATION_NAME)
        .build();
  }
}
