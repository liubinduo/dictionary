package com.v1ok.dictionary.db.service.impl.support;

import lombok.Data;

@Data
public class Districts {

  private Object citycode;
  private String adcode;
  private String name;
  private String center;
  private String level;
  private Districts[] districts;
}
