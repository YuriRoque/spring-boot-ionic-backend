package com.yuri.cursomc.config;

import java.text.ParseException;

import com.yuri.cursomc.services.DBService;
import com.yuri.cursomc.services.EmailService;
import com.yuri.cursomc.services.MockEmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestConfig {
  
  @Autowired
  private DBService dbService;

  @Bean
  public boolean instantiateDatabase() throws ParseException {

    dbService.instantiateTesteDatabase();

    return true;

  }
  
  @Bean
  public EmailService emailService() {
    return new MockEmailService();
  }

}
