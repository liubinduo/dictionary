package com.v1ok.dictionary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootApplication
public class StartApplication {


  public static void main(String[] args) {
    SpringApplication.run(StartApplication.class, args);
  }
}
