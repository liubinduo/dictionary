package com.v1ok.dictionary;

import com.v1ok.dictionary.db.service.IDistrictsService;
import io.ebean.config.CurrentUserProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


@Configuration
@Slf4j
public class ApplicationConfiguration {

  @Bean
  public CurrentUserProvider currentUserProvider() {
    return new CurrentUserProvider() {
      @Override
      public Object currentUser() {
        return null;
      }
    };
  }

  @Bean
  public RestTemplate restTemplate() {
    RestTemplate template = new RestTemplate(getFactory());
    return template;
  }

  private OkHttp3ClientHttpRequestFactory getFactory() {
    return new OkHttp3ClientHttpRequestFactory();
  }


  @Bean
  public ApplicationListener<ContextRefreshedEvent> refreshedEventApplicationListener(
      IDistrictsService districtsService) {
    return event -> {
      try {
        districtsService.initData();
      } catch (Exception e) {
        log.error("", e);
      }

      System.out.println("========");
    };
  }

}
