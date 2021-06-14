package com.v1ok.dictionary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.v1ok.auth.IUserContext;
import com.v1ok.commons.ContextHolder;
import com.v1ok.commons.IContext;
import com.v1ok.commons.converter.json.TextPlanMappingJackson2HttpMessageConverter;
import com.v1ok.db.util.UUIDGenerator;
import com.v1ok.dictionary.db.service.IDistrictsService;
import com.v1ok.uuid.IDGenerate;
import io.ebean.config.CurrentTenantProvider;
import io.ebean.config.CurrentUserProvider;
import io.ebean.config.DatabaseConfig;
import io.ebean.config.TenantMode;
import io.ebean.spring.txn.SpringJdbcTransactionManager;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@Slf4j
public class ApplicationConfiguration implements WebMvcConfigurer {

  public final static String SYSTEM_ID = "DISTRICTS_SERVICE";

  final ObjectMapper objectMapper;

  public ApplicationConfiguration(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
    this.objectMapper.setFilterProvider(new SimpleFilterProvider().setFailOnUnknownId(false));
  }

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

    TextPlanMappingJackson2HttpMessageConverter converter = new TextPlanMappingJackson2HttpMessageConverter(
        objectMapper);
    converters.add(converter);
  }


  @Bean
  public CurrentUserProvider userProvider() {
    return () -> {
      IContext iContext = ContextHolder.getHolder().get();

      if (iContext == null) {
        return null;
      }
      IUserContext userContext = iContext.currentUser();

      //TODO 这里应该是岗位ID
      return userContext.getUserId();
    };
  }

//  @Bean
//  public CurrentTenantProvider tenantProvider() {
//    return () -> {
//      IContext iContext = ContextHolder.getHolder().get();
//      if (iContext == null) {
//        return null;
//      }
//      IUserContext userContext = iContext.currentUser();
//      return userContext.getTenantId();
//    };
//  }


  @Bean
  public DatabaseConfig serverConfig(
      @Autowired DataSource dataSource,
      UUIDGenerator uuidGenerator,
      CurrentUserProvider currentUser
  ) {

    DatabaseConfig config = new DatabaseConfig();

    config.setName("db");
    config.setCurrentUserProvider(currentUser);

    // set the spring's datasource and transaction manager.
    config.setDataSource(dataSource);
    config.setExternalTransactionManager(new SpringJdbcTransactionManager());

    config.loadFromProperties();

    // set as default and register so that Model can be
    // used if desired for save() and update() etc
    config.setDefaultServer(true);
    config.setRegister(true);

    // set UUID Generator
    config.add(uuidGenerator);

    // set multi tenancy
//    config.setTenantMode(TenantMode.PARTITION);
//    config.setCurrentTenantProvider(tenantProvider);

    return config;
  }

  @Bean
  public UUIDGenerator generator(@Autowired(required = false) IDGenerate generate) {
    return new UUIDGenerator(generate);
  }


  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate(getFactory());
  }

  private OkHttp3ClientHttpRequestFactory getFactory() {

    Builder builder = new Builder().callTimeout(5, TimeUnit.MINUTES);

    return new OkHttp3ClientHttpRequestFactory(
        builder.build());
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
